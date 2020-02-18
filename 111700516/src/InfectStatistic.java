package 疫情统计;

import java.util.Calendar;
import java.util.*;
import java.util.regex.Pattern;
import java.util.List;
import java.util.Vector;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
public class InfectStatistic 
{
	 static Vector<MessageItem> items;
	 
	 /* 判断两个日期date1是否小于等于date2 */
	 public boolean IsInclude(String date1,String date2)
	 {
		 if(date1.equals(date2))
			 return true;
		 Integer i=date2.compareTo(date1);
		 if(i>0)
			 return true;
		 else
			 return false;
	 }
	 /* 返回去掉文件名 */
	 public String toFileName(String s)
	 {
		 char[] a=s.toCharArray();
		 char[] temp = new char[10];
		 for(int i=0;i<10;i++)
		 {
			 temp[i]=a[i];
		 }
		 String result=new String(temp);
		 return result;
	 }
	 
	/* 集合所有符合date日期之前的日期对应的文件里的信息 */
	public void messageCollect(String date,Vector<MessageItem> item,CommandLineHandler command)
	{
		String path=command.returnLog();//path: 读取日志的路径
		String pathout=command.returnOut();//pathout：输出文档的路径
		
		File file=new File(path);
		File[] fileList=file.listFiles();
		Vector<MessageItem> myItem=item;
		FileHandler a=new FileHandler();
		for(int i=0;i<fileList.length;i++)
		{
			//把所有小等于这个日期的文件都查一遍
			if(IsInclude(toFileName(fileList[i].getName()),date) && fileList[i].getName()!="output.txt")
			{			
				myItem=a.openFile(fileList[i].getPath(),myItem);	
			}
		}
		//计算全国的总和
		myItem=a.sumUp(myItem);
		
		myItem=command.setProvince(myItem);
		//生成输出文件
		a.outputFile(pathout, myItem,command.returnType()[0],command.returnType()[1],command.returnType()[2],command.returnType()[3]);
	}
	
	 public static void main(String arg[])
	 {
		 String province[] = {"全国" , "安徽" , "北京" , "重庆" , "福建" , "甘肃" , "广东" ," 广西" , "贵州" , "海南" , "河北" , "河南" , "黑龙江" , "湖北" , "湖南" , "吉林" , "江苏" , "江西" , "辽宁" , " 内蒙古" , " 宁夏" , "青海" , "山东" , "山西" , "陕西" , "上海" , "四川" , "天津" , "西藏" , "新疆" , "云南" , "浙江"};
		 items=new Vector<MessageItem>();
		 for (int i = 0 ; i < 32 ; i++)
		 {				
			 items.add(new MessageItem(province[i]));
		 }
				 
		 //解析命令行，传入arg[]为参数
		 CommandLineHandler myCommand=new CommandLineHandler(arg);
		 items=myCommand.setStatus(items);
		 InfectStatistic myMain=new InfectStatistic();
		 //有无填时间
		 if(myCommand.returnDate()!=null)
			 myMain.messageCollect(myCommand.returnDate(),items,myCommand);
		 else
		 {
			 Calendar cal = Calendar.getInstance();//得到当前时间 
			 String date=String.valueOf(cal.get(Calendar.YEAR))+"-"+String.valueOf(cal.get(Calendar.MONTH) + 1)+"-"+String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
			 myMain.messageCollect(date,items,myCommand);
		 }
	 }
}
class MessageItem 
{
		String name;//省名
		int infectItem;//感染患者人数
		int doubtItem;//疑似患者人数
		int healthyItem;//治愈人数
		int deathItem;//死亡人数
		int isOut;//是否被输出
		boolean isChanged;//省份的isout是否已被修改
		/* 初始化 */
		public MessageItem(String Name)
		{
			name=Name;
			infectItem = 0;
			doubtItem = 0;
			healthyItem = 0;
			deathItem = 0;
			isOut=0;
			isChanged=false;
		}
		
		/* 获得省名 */
		public String getName()
		{
			return name;
		}
		
		/* 新增感染患者人数 */
		public void addInfect(int c)
		{
			infectItem+=c;
		}
		
		/* 减少感染患者人数 */
		public void subtractInfect(int c)
		{
			infectItem-=c;
		}
		/* 获得感染患者人数 */
		public int getInfect()
		{
			return infectItem;
		}

		/* 新增疑似患者人数 */
		public void addDoubt(int c)
		{
			doubtItem+=c;
		}
		
		/* 减少疑似患者人数 */
		public void subtractDoubt(int c)
		{
			doubtItem-=c;
		}
		
		/* 获得疑似患者人数 */
		public int getDoubt()
		{
			return doubtItem;
		}
		
		/* 新增治愈人数 */
		public void addhealth(int c)
		{
			healthyItem+=c;
		}
		
		/* 获得治愈人数 */
		public int getHealth()
		{
			return healthyItem;
		}
		
		/* 新增死亡人数 */
		public void addDeath(int c)
		{
			deathItem+=c;
		}
		
		/* 获取死亡人数 */
		public int getDeath()
		{
			return deathItem;
		}
		
		/* 将isOut改成能输出的 */
		public void setIsOut()
		{
			isOut=1;
		}
		
		/* 将isOut改成不能输出的 */
		public void setUnOut()
		{
			isOut=0;
		}
		
		/* 获取是否被输出 */
		public boolean out()
		{
			if(isOut==0)
				return false;
			else
				return true;
		}
		
		/* 设置省份isout已被修改  */
		public void setChanged()
		{
			isChanged=true;
		}
		
		/* 返回省份isout是否被改*/
		public boolean getChanged()
		{
			return isChanged;
		}
}

/* 正则表达式处理类 */
class RegexUtil 
{
	public String r1="\\W+ 新增 感染患者 \\d+人";
	public String r2="\\W+ 新增 疑似患者 \\d+人";
	public String r3="\\W+ 感染患者 流入 \\W+ \\d+人";
	public String r4="\\W+ 疑似患者 流入 \\W+ \\d+人";
	public String r5="\\W+ 死亡 \\d+人";
	public String r6="\\W+ 治愈 \\d+人";
	public String r7="\\W+ 疑似患者 确诊感染 \\d+人";
	public String r8="\\W+ 排除 疑似患者 \\d+人";
	Vector<MessageItem> message;//装各省份信息的向量
	public RegexUtil(Vector<MessageItem> a)
	{
		message=a;
	}
	
	/* 获取r1正则表达式参数 */
	public void getParameter1(String s)
	{
		String n1=" 新增";
		String n2="感染患者 ";
		String n3="人";
		//提取参数
		String province=s.split(n1)[0];
		String temp=s.split(n2)[1];
		String count=temp.split(n3)[0];
		
		//寻找相应省的MessageItem对象,并更改人数
		for(int i=0;i<32;i++)
		{
			if(province.equals(message.get(i).getName()))
			{
				message.get(i).addInfect(Integer.parseInt(count));
				message.get(i).setIsOut();
			}
		}
	}
	
	/* 获取r2正则表达式参数 */
	public void getParameter2(String s)
	{
		String n1=" 新增";
		String n2="疑似患者 ";
		String n3="人";
		
		//提取参数
		String province=s.split(n1)[0];
		String temp=s.split(n2)[1];
		String count=temp.split(n3)[0];
		
		//寻找相应省的MessageItem对象,并更改人数
		for(int i=0;i<32;i++)
		{
			if(province.equals(message.get(i).getName()))
			{
				message.get(i).addDoubt(Integer.parseInt(count));
				message.get(i).setIsOut();
			}
		}
	}
	
	/* 获取r3正则表达式参数 */
	public void getParameter3(String s)
	{
		String n1=" 感染患者";
		String n2="流入 ";
		String n3="人";
		String n4=" ";
		
		//提取参数
		String province1=s.split(n1)[0];
		String temp1=s.split(n2)[1];
		String province2=temp1.split(n4)[0];
		String temp2=temp1.split(n4)[1];
		String count=temp2.split(n3)[0];
		
		//寻找相应省的MessageItem对象,并更改人数
		for(int i=0;i<32;i++)
		{
			if(province1.equals(message.get(i).getName()))
			{
				message.get(i).subtractInfect(Integer.parseInt(count));
				message.get(i).setIsOut();
			}
			if(province2.equals(message.get(i).getName()))
			{
				message.get(i).addInfect(Integer.parseInt(count));
				message.get(i).setIsOut();
			}
		}
	}
	
	/* 获取r4正则表达式参数 */
	public void getParameter4(String s)
	{
		String n1=" 疑似患者";
		String n2="流入 ";
		String n3="人";
		String n4=" ";
		
		//提取参数
		String province1=s.split(n1)[0];
		String temp1=s.split(n2)[1];
		String province2=temp1.split(n4)[0];
		String temp2=temp1.split(n4)[1];
		String count=temp2.split(n3)[0];
		
		//寻找相应省的MessageItem对象,并更改人数
		for(int i=0;i<32;i++)
		{
			if(province1.equals(message.get(i).getName()))
			{
				message.get(i).subtractDoubt(Integer.parseInt(count));
				message.get(i).setIsOut();
			}
			if(province2.equals(message.get(i).getName()))
			{
				message.get(i).addDoubt(Integer.parseInt(count));
				message.get(i).setIsOut();
			}
		}
	}
	
	/* 获取r5正则表达式参数 */
	public void getParameter5(String s)
	{
		String n1=" 死亡 ";
		String n3="人";
		//提取参数
		String province=s.split(n1)[0];
		String temp=s.split(n1)[1];
		String count=temp.split(n3)[0];
		
		//寻找相应省的MessageItem对象,并更改人数
		for(int i=0;i<32;i++)
		{
			if(province.equals(message.get(i).getName()))
			{
				message.get(i).addDeath(Integer.parseInt(count));
				message.get(i).subtractInfect(Integer.parseInt(count));
				message.get(i).setIsOut();
			}
		}
	}
	
	/* 获取r6正则表达式参数 */
	public void getParameter6(String s)
	{
		String n1=" 治愈 ";
		String n3="人";
		//提取参数
		String province=s.split(n1)[0];
		String temp=s.split(n1)[1];
		String count=temp.split(n3)[0];
		
		//寻找相应省的MessageItem对象,并更改人数
		for(int i=0;i<32;i++)
		{
			if(province.equals(message.get(i).getName()))
			{
				message.get(i).addhealth(Integer.parseInt(count));
				message.get(i).subtractInfect(Integer.parseInt(count));
				message.get(i).setIsOut();
			}
		}
	}
	
	/* 获取r7正则表达式参数 */
	public void getParameter7(String s)
	{
		String n1=" 疑似患者";
		String n2="确诊感染 ";
		String n3="人";
		
		//提取参数
		String province=s.split(n1)[0];
		String temp=s.split(n2)[1];
		String count=temp.split(n3)[0];
		
		//寻找相应省的MessageItem对象,并更改人数
		for(int i=0;i<32;i++)
		{
			if(province.equals(message.get(i).getName()))
			{
				message.get(i).addInfect(Integer.parseInt(count));
				message.get(i).subtractDoubt(Integer.parseInt(count));
				message.get(i).setIsOut();
			}
		}
	}
	
	/* 获取r8正则表达式参数 */
	public void getParameter8(String s)
	{
		String n1=" 排除";
		String n2="疑似患者 ";
		String n3="人";
		
		//提取参数
		String province=s.split(n1)[0];
		String temp=s.split(n2)[1];
		String count=temp.split(n3)[0];
		
		//寻找相应省的MessageItem对象,并更改人数
		for(int i=0;i<32;i++)
		{
			if(province.equals(message.get(i).getName()))
			{
				message.get(i).subtractDoubt(Integer.parseInt(count));
				message.get(i).setIsOut();
			}
		}
	}
	
	/* 获取各省份的信息向量 */
	public Vector<MessageItem> getItems()
	{
		return message;
	}

}

/* 处理命令行类 */
class CommandLineHandler
{
	String ARG[];//传入main函数的参数arg[]
	
	boolean list;
	boolean log;
	boolean out;
	boolean date;
	boolean type;
	boolean province;
	
	String lo;
	String ou;
	String da;
	int ty[];
	List<String> pro;
	
	public CommandLineHandler(String a[])
	{
		ARG=a;
	    list=false;
		log=false;
		out=false;
		date=false;
		type=false;
		province=false;
		
		lo=null;
		ou=null;
		da=null;
		
		ty=new int[4];//按 ip：感染患者，sp： 疑似患者，cure：治愈 ，dead：死亡 排序
		for(int i=0;i<4;i++)
			ty[i]=0;
		
		pro=new ArrayList<String>();
	}
	/* 设置状态,并赋值 */
	public Vector<MessageItem> setStatus(Vector<MessageItem> item)
	{
		Vector<MessageItem> a=item;
		for(int i=0;i<ARG.length;i++)
		{
			if(ARG[i].equals("list"))
				list=true;
			if(ARG[i].equals("-log"))
				log=true;
			if(ARG[i].equals("-out"))
				out=true;
			if(ARG[i].equals("-date"))
				date=true;
			if(ARG[i].equals("-type"))
				type=true;
			if(ARG[i].equals("-province"))
				province=true;
		}
		
		getLog();
		getOut();
		getType();
		getDate();
		a=setProvince(a);
		return a;
	}
	
	/* 获得log,即指定日志目录的位置 */
	public void getLog()
	{
		if(log)
		{
			for(int i=0;i<ARG.length;i++)
			{
				if(ARG[i].equals("-log"))
					lo= ARG[i+1];
			}
		}
	}
	
	/* 获得out,即输出文件路径和文件名 */
	public void getOut()
	{
		if(out)
		{
			for(int i=0;i<ARG.length;i++)
			{
				if(ARG[i].equals("-out"))
					ou= ARG[i+1];
			}
		}
	}
	
	/* 获得date,即指定日期 */
	public void getDate()
	{
		if(date)
		{
			for(int i=0;i<ARG.length;i++)
			{
				if(ARG[i].equals("-date"))
					da= ARG[i+1];
			}
		}
	}
	
	/* 获得type,即患者类型*/
	public void getType()
	{
		if(type)
		{
			for(int i=0;i<ARG.length;i++)
			{
				if(ARG[i].equals("-type"))
				{
					int flag=1;//几种情况输出的次序
					for(int n=i+1;n<ARG.length && !ARG[n].equals("-log") && !ARG[n].equals("-out") && !ARG[n].equals("-date") && !ARG[n].equals("-type");n++)
					{
						if(ARG[n].equals("ip"))
							ty[0]=flag;
						if(ARG[n].equals("sp"))
							ty[1]=flag;
						if(ARG[n].equals("cure"))
							ty[2]=flag;
						if(ARG[n].equals("dead"))
							ty[3]=flag;
						flag++;
					}
				}
			}
		}
	}
	
	/* 将相应省份输出位置1 */
	public Vector<MessageItem> setProvince(Vector<MessageItem> item)
	{

		 Vector<MessageItem> result=item;
		 if(province)
		 {
			for(int i=0;i<ARG.length;i++)
			{
				if(ARG[i].equals("-province"))
				{
					for(int n=i+1; n<ARG.length && !ARG[n].equals("-log") && !ARG[n].equals("-out") && !ARG[n].equals("-date") && !ARG[n].equals("-type");n++)
					{
						 for(int k=0;k<32;k++)
						 {
							if(result.get(k).getName().equals(ARG[n]))
							{
								result.get(k).setIsOut();
								result.get(k).setChanged();
							}
							else if(!result.get(k).getChanged())
								result.get(k).setUnOut();
						 }
					}
				}
				
			}
		}
		 //若不指明province则一定有全国
		 if(!province)
			 result.get(0).setIsOut();
		return result;
	}
	
	/* 获得log */
	public String returnLog()
	{
		return lo;
	}
	
	/* 获得out */
	public String returnOut()
	{
		return ou;
	}
	
	/* 获得date */
	public String returnDate()
	{
		return da;
	}
	
	/* 获得type */
	public int[] returnType()
	{
		return ty;
	}
	
	/* 获得province */
	public List<String> returnProvince()
	{
		return pro;
	}
	
	/* 获得句子里是否存在province[] */
	public boolean isPro()
	{
		return province;
	}
	/* 获得句子里是否存在list */
	public boolean isList()
	{
		return list;
	}
}
/* 处理文件类  */
class FileHandler
{
	//打开日志文件,fileName为文件路径，a为初始的省份储存信息的对象向量，返回处理完的省份对象向量
	public Vector<MessageItem> openFile(String fileName,Vector<MessageItem> a)
	{
		 Vector<MessageItem> items=a;
		 try 
		 {
			 //try代码块，当发生异常时会转到catch代码块中
	         //读取指定的文件
	         BufferedReader in = new BufferedReader(new FileReader(fileName));
	         String str=null;
	         while ((str = in.readLine())!= null) 
	         {
		        RegexUtil b=new RegexUtil(items);
		        
		        //匹配正则表达式
		        if(str.matches(b.r1))
		        {
		        	b.getParameter1(str);
		        }
		        if(str.matches(b.r2))
		        {
		        	b.getParameter2(str);
		        }
		        if(str.matches(b.r3))
		        {
		        	b.getParameter3(str);
		        }
		        if(str.matches(b.r4))
		        {
		        	b.getParameter4(str);
		        }
		        if(str.matches(b.r5))
		        {
		        	b.getParameter5(str);
		        }
		        if(str.matches(b.r6))
		        {
		        	b.getParameter6(str);
		        }
		        if(str.matches(b.r7))
		        {
		        	b.getParameter7(str);
		        }
		        if(str.matches(b.r8))
		        {
		        	b.getParameter8(str);
		        }
		        items=b.getItems();
	         }
	         in.close();
	     } 
		 catch (IOException e) 
		 {
	         e.printStackTrace();
	     }
		 return items;
	}
	
	/* 计算全国的项 */
	public Vector<MessageItem> sumUp(Vector<MessageItem> item)
	{
		Vector<MessageItem> result=item;
		for(int i=1;i<32;i++)
		{
			result.get(0).addInfect(result.get(i).getInfect());
			result.get(0).addDoubt(result.get(i).getDoubt());
			result.get(0).addDeath(result.get(i).getDeath());
			result.get(0).addhealth(result.get(i).getHealth());
		}
		return result;
	}
	
	/* 生成输出文件，fileName为文件名字，a为储存省份信息的向量，ip~death为type是否输出的标志 */
	public void outputFile(String fileName,Vector<MessageItem> a,int ip,int sp,int cure,int dead)
	{
		File file = new File(fileName);
		if (file.exists()) 
		{
			// 检查目标文件是否存在,存在则删除			
			file.delete();		
		}	
		try (PrintWriter output = new PrintWriter(file);) 
		{			
			
			for(int i=0;i<32;i++)
			{			
				if(a.get(i).out())
				{
					String s=a.get(i).getName();//将字符串初始化为省份的名字
					if(ip==0 && sp==0 && cure==0 && dead==0)//ip=0即输出文件时不用忽略感染患者......
						s+=" 感染患者"+a.get(i).getInfect()+"人"+" 疑似患者"+a.get(i).getDoubt()+"人"+" 治愈"+a.get(i).getHealth()+"人"+" 死亡"+a.get(i).getDeath()+"人";
					else
					{
						if(ip==1)	
							s+=" 感染患者"+a.get(i).getInfect()+"人";
						if(sp==1)
							s+=" 疑似患者"+a.get(i).getDoubt()+"人";
						if(cure==1)
							s+=" 治愈"+a.get(i).getHealth()+"人";
						if(dead==1)
							s+=" 死亡"+a.get(i).getDeath()+"人";
						
						if(ip==2)	
							s+=" 感染患者"+a.get(i).getInfect()+"人";
						if(sp==2)
							s+=" 疑似患者"+a.get(i).getDoubt()+"人";
						if(cure==2)
							s+=" 治愈"+a.get(i).getHealth()+"人";
						if(dead==2)
							s+=" 死亡"+a.get(i).getDeath()+"人";
						
						if(ip==3)	
							s+=" 感染患者"+a.get(i).getInfect()+"人";
						if(sp==3)
							s+=" 疑似患者"+a.get(i).getDoubt()+"人";
						if(cure==3)
							s+=" 治愈"+a.get(i).getHealth()+"人";
						if(dead==3)
							s+=" 死亡"+a.get(i).getDeath()+"人";
						
						if(ip==4)	
							s+=" 感染患者"+a.get(i).getInfect()+"人";
						if(sp==4)
							s+=" 疑似患者"+a.get(i).getDoubt()+"人";
						if(cure==4)
							s+=" 治愈"+a.get(i).getHealth()+"人";
						if(dead==4)
							s+=" 死亡"+a.get(i).getDeath()+"人";
					}
					
					output.println(s);
				}
			}
			output.println("// 该文档并非真实数据，仅供测试使用");
		}
		 catch (IOException e) 
		 {
	         e.printStackTrace();
	     }
	}
}
