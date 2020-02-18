package ����ͳ��;

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
	 
	 /* �ж���������date1�Ƿ�С�ڵ���date2 */
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
	 /* ����ȥ���ļ��� */
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
	 
	/* �������з���date����֮ǰ�����ڶ�Ӧ���ļ������Ϣ */
	public void messageCollect(String date,Vector<MessageItem> item,CommandLineHandler command)
	{
		String path=command.returnLog();//path: ��ȡ��־��·��
		String pathout=command.returnOut();//pathout������ĵ���·��
		
		File file=new File(path);
		File[] fileList=file.listFiles();
		Vector<MessageItem> myItem=item;
		FileHandler a=new FileHandler();
		for(int i=0;i<fileList.length;i++)
		{
			//������С����������ڵ��ļ�����һ��
			if(IsInclude(toFileName(fileList[i].getName()),date) && fileList[i].getName()!="output.txt")
			{			
				myItem=a.openFile(fileList[i].getPath(),myItem);	
			}
		}
		//����ȫ�����ܺ�
		myItem=a.sumUp(myItem);
		
		myItem=command.setProvince(myItem);
		//��������ļ�
		a.outputFile(pathout, myItem,command.returnType()[0],command.returnType()[1],command.returnType()[2],command.returnType()[3]);
	}
	
	 public static void main(String arg[])
	 {
		 String province[] = {"ȫ��" , "����" , "����" , "����" , "����" , "����" , "�㶫" ," ����" , "����" , "����" , "�ӱ�" , "����" , "������" , "����" , "����" , "����" , "����" , "����" , "����" , " ���ɹ�" , " ����" , "�ຣ" , "ɽ��" , "ɽ��" , "����" , "�Ϻ�" , "�Ĵ�" , "���" , "����" , "�½�" , "����" , "�㽭"};
		 items=new Vector<MessageItem>();
		 for (int i = 0 ; i < 32 ; i++)
		 {				
			 items.add(new MessageItem(province[i]));
		 }
				 
		 //���������У�����arg[]Ϊ����
		 CommandLineHandler myCommand=new CommandLineHandler(arg);
		 items=myCommand.setStatus(items);
		 InfectStatistic myMain=new InfectStatistic();
		 //������ʱ��
		 if(myCommand.returnDate()!=null)
			 myMain.messageCollect(myCommand.returnDate(),items,myCommand);
		 else
		 {
			 Calendar cal = Calendar.getInstance();//�õ���ǰʱ�� 
			 String date=String.valueOf(cal.get(Calendar.YEAR))+"-"+String.valueOf(cal.get(Calendar.MONTH) + 1)+"-"+String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
			 myMain.messageCollect(date,items,myCommand);
		 }
	 }
}
class MessageItem 
{
		String name;//ʡ��
		int infectItem;//��Ⱦ��������
		int doubtItem;//���ƻ�������
		int healthyItem;//��������
		int deathItem;//��������
		int isOut;//�Ƿ����
		boolean isChanged;//ʡ�ݵ�isout�Ƿ��ѱ��޸�
		/* ��ʼ�� */
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
		
		/* ���ʡ�� */
		public String getName()
		{
			return name;
		}
		
		/* ������Ⱦ�������� */
		public void addInfect(int c)
		{
			infectItem+=c;
		}
		
		/* ���ٸ�Ⱦ�������� */
		public void subtractInfect(int c)
		{
			infectItem-=c;
		}
		/* ��ø�Ⱦ�������� */
		public int getInfect()
		{
			return infectItem;
		}

		/* �������ƻ������� */
		public void addDoubt(int c)
		{
			doubtItem+=c;
		}
		
		/* �������ƻ������� */
		public void subtractDoubt(int c)
		{
			doubtItem-=c;
		}
		
		/* ������ƻ������� */
		public int getDoubt()
		{
			return doubtItem;
		}
		
		/* ������������ */
		public void addhealth(int c)
		{
			healthyItem+=c;
		}
		
		/* ����������� */
		public int getHealth()
		{
			return healthyItem;
		}
		
		/* ������������ */
		public void addDeath(int c)
		{
			deathItem+=c;
		}
		
		/* ��ȡ�������� */
		public int getDeath()
		{
			return deathItem;
		}
		
		/* ��isOut�ĳ�������� */
		public void setIsOut()
		{
			isOut=1;
		}
		
		/* ��isOut�ĳɲ�������� */
		public void setUnOut()
		{
			isOut=0;
		}
		
		/* ��ȡ�Ƿ���� */
		public boolean out()
		{
			if(isOut==0)
				return false;
			else
				return true;
		}
		
		/* ����ʡ��isout�ѱ��޸�  */
		public void setChanged()
		{
			isChanged=true;
		}
		
		/* ����ʡ��isout�Ƿ񱻸�*/
		public boolean getChanged()
		{
			return isChanged;
		}
}

/* ������ʽ������ */
class RegexUtil 
{
	public String r1="\\W+ ���� ��Ⱦ���� \\d+��";
	public String r2="\\W+ ���� ���ƻ��� \\d+��";
	public String r3="\\W+ ��Ⱦ���� ���� \\W+ \\d+��";
	public String r4="\\W+ ���ƻ��� ���� \\W+ \\d+��";
	public String r5="\\W+ ���� \\d+��";
	public String r6="\\W+ ���� \\d+��";
	public String r7="\\W+ ���ƻ��� ȷ���Ⱦ \\d+��";
	public String r8="\\W+ �ų� ���ƻ��� \\d+��";
	Vector<MessageItem> message;//װ��ʡ����Ϣ������
	public RegexUtil(Vector<MessageItem> a)
	{
		message=a;
	}
	
	/* ��ȡr1������ʽ���� */
	public void getParameter1(String s)
	{
		String n1=" ����";
		String n2="��Ⱦ���� ";
		String n3="��";
		//��ȡ����
		String province=s.split(n1)[0];
		String temp=s.split(n2)[1];
		String count=temp.split(n3)[0];
		
		//Ѱ����Ӧʡ��MessageItem����,����������
		for(int i=0;i<32;i++)
		{
			if(province.equals(message.get(i).getName()))
			{
				message.get(i).addInfect(Integer.parseInt(count));
				message.get(i).setIsOut();
			}
		}
	}
	
	/* ��ȡr2������ʽ���� */
	public void getParameter2(String s)
	{
		String n1=" ����";
		String n2="���ƻ��� ";
		String n3="��";
		
		//��ȡ����
		String province=s.split(n1)[0];
		String temp=s.split(n2)[1];
		String count=temp.split(n3)[0];
		
		//Ѱ����Ӧʡ��MessageItem����,����������
		for(int i=0;i<32;i++)
		{
			if(province.equals(message.get(i).getName()))
			{
				message.get(i).addDoubt(Integer.parseInt(count));
				message.get(i).setIsOut();
			}
		}
	}
	
	/* ��ȡr3������ʽ���� */
	public void getParameter3(String s)
	{
		String n1=" ��Ⱦ����";
		String n2="���� ";
		String n3="��";
		String n4=" ";
		
		//��ȡ����
		String province1=s.split(n1)[0];
		String temp1=s.split(n2)[1];
		String province2=temp1.split(n4)[0];
		String temp2=temp1.split(n4)[1];
		String count=temp2.split(n3)[0];
		
		//Ѱ����Ӧʡ��MessageItem����,����������
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
	
	/* ��ȡr4������ʽ���� */
	public void getParameter4(String s)
	{
		String n1=" ���ƻ���";
		String n2="���� ";
		String n3="��";
		String n4=" ";
		
		//��ȡ����
		String province1=s.split(n1)[0];
		String temp1=s.split(n2)[1];
		String province2=temp1.split(n4)[0];
		String temp2=temp1.split(n4)[1];
		String count=temp2.split(n3)[0];
		
		//Ѱ����Ӧʡ��MessageItem����,����������
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
	
	/* ��ȡr5������ʽ���� */
	public void getParameter5(String s)
	{
		String n1=" ���� ";
		String n3="��";
		//��ȡ����
		String province=s.split(n1)[0];
		String temp=s.split(n1)[1];
		String count=temp.split(n3)[0];
		
		//Ѱ����Ӧʡ��MessageItem����,����������
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
	
	/* ��ȡr6������ʽ���� */
	public void getParameter6(String s)
	{
		String n1=" ���� ";
		String n3="��";
		//��ȡ����
		String province=s.split(n1)[0];
		String temp=s.split(n1)[1];
		String count=temp.split(n3)[0];
		
		//Ѱ����Ӧʡ��MessageItem����,����������
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
	
	/* ��ȡr7������ʽ���� */
	public void getParameter7(String s)
	{
		String n1=" ���ƻ���";
		String n2="ȷ���Ⱦ ";
		String n3="��";
		
		//��ȡ����
		String province=s.split(n1)[0];
		String temp=s.split(n2)[1];
		String count=temp.split(n3)[0];
		
		//Ѱ����Ӧʡ��MessageItem����,����������
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
	
	/* ��ȡr8������ʽ���� */
	public void getParameter8(String s)
	{
		String n1=" �ų�";
		String n2="���ƻ��� ";
		String n3="��";
		
		//��ȡ����
		String province=s.split(n1)[0];
		String temp=s.split(n2)[1];
		String count=temp.split(n3)[0];
		
		//Ѱ����Ӧʡ��MessageItem����,����������
		for(int i=0;i<32;i++)
		{
			if(province.equals(message.get(i).getName()))
			{
				message.get(i).subtractDoubt(Integer.parseInt(count));
				message.get(i).setIsOut();
			}
		}
	}
	
	/* ��ȡ��ʡ�ݵ���Ϣ���� */
	public Vector<MessageItem> getItems()
	{
		return message;
	}

}

/* ������������ */
class CommandLineHandler
{
	String ARG[];//����main�����Ĳ���arg[]
	
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
		
		ty=new int[4];//�� ip����Ⱦ���ߣ�sp�� ���ƻ��ߣ�cure������ ��dead������ ����
		for(int i=0;i<4;i++)
			ty[i]=0;
		
		pro=new ArrayList<String>();
	}
	/* ����״̬,����ֵ */
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
	
	/* ���log,��ָ����־Ŀ¼��λ�� */
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
	
	/* ���out,������ļ�·�����ļ��� */
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
	
	/* ���date,��ָ������ */
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
	
	/* ���type,����������*/
	public void getType()
	{
		if(type)
		{
			for(int i=0;i<ARG.length;i++)
			{
				if(ARG[i].equals("-type"))
				{
					int flag=1;//�����������Ĵ���
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
	
	/* ����Ӧʡ�����λ��1 */
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
		 //����ָ��province��һ����ȫ��
		 if(!province)
			 result.get(0).setIsOut();
		return result;
	}
	
	/* ���log */
	public String returnLog()
	{
		return lo;
	}
	
	/* ���out */
	public String returnOut()
	{
		return ou;
	}
	
	/* ���date */
	public String returnDate()
	{
		return da;
	}
	
	/* ���type */
	public int[] returnType()
	{
		return ty;
	}
	
	/* ���province */
	public List<String> returnProvince()
	{
		return pro;
	}
	
	/* ��þ������Ƿ����province[] */
	public boolean isPro()
	{
		return province;
	}
	/* ��þ������Ƿ����list */
	public boolean isList()
	{
		return list;
	}
}
/* �����ļ���  */
class FileHandler
{
	//����־�ļ�,fileNameΪ�ļ�·����aΪ��ʼ��ʡ�ݴ�����Ϣ�Ķ������������ش������ʡ�ݶ�������
	public Vector<MessageItem> openFile(String fileName,Vector<MessageItem> a)
	{
		 Vector<MessageItem> items=a;
		 try 
		 {
			 //try����飬�������쳣ʱ��ת��catch�������
	         //��ȡָ�����ļ�
	         BufferedReader in = new BufferedReader(new FileReader(fileName));
	         String str=null;
	         while ((str = in.readLine())!= null) 
	         {
		        RegexUtil b=new RegexUtil(items);
		        
		        //ƥ��������ʽ
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
	
	/* ����ȫ������ */
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
	
	/* ��������ļ���fileNameΪ�ļ����֣�aΪ����ʡ����Ϣ��������ip~deathΪtype�Ƿ�����ı�־ */
	public void outputFile(String fileName,Vector<MessageItem> a,int ip,int sp,int cure,int dead)
	{
		File file = new File(fileName);
		if (file.exists()) 
		{
			// ���Ŀ���ļ��Ƿ����,������ɾ��			
			file.delete();		
		}	
		try (PrintWriter output = new PrintWriter(file);) 
		{			
			
			for(int i=0;i<32;i++)
			{			
				if(a.get(i).out())
				{
					String s=a.get(i).getName();//���ַ�����ʼ��Ϊʡ�ݵ�����
					if(ip==0 && sp==0 && cure==0 && dead==0)//ip=0������ļ�ʱ���ú��Ը�Ⱦ����......
						s+=" ��Ⱦ����"+a.get(i).getInfect()+"��"+" ���ƻ���"+a.get(i).getDoubt()+"��"+" ����"+a.get(i).getHealth()+"��"+" ����"+a.get(i).getDeath()+"��";
					else
					{
						if(ip==1)	
							s+=" ��Ⱦ����"+a.get(i).getInfect()+"��";
						if(sp==1)
							s+=" ���ƻ���"+a.get(i).getDoubt()+"��";
						if(cure==1)
							s+=" ����"+a.get(i).getHealth()+"��";
						if(dead==1)
							s+=" ����"+a.get(i).getDeath()+"��";
						
						if(ip==2)	
							s+=" ��Ⱦ����"+a.get(i).getInfect()+"��";
						if(sp==2)
							s+=" ���ƻ���"+a.get(i).getDoubt()+"��";
						if(cure==2)
							s+=" ����"+a.get(i).getHealth()+"��";
						if(dead==2)
							s+=" ����"+a.get(i).getDeath()+"��";
						
						if(ip==3)	
							s+=" ��Ⱦ����"+a.get(i).getInfect()+"��";
						if(sp==3)
							s+=" ���ƻ���"+a.get(i).getDoubt()+"��";
						if(cure==3)
							s+=" ����"+a.get(i).getHealth()+"��";
						if(dead==3)
							s+=" ����"+a.get(i).getDeath()+"��";
						
						if(ip==4)	
							s+=" ��Ⱦ����"+a.get(i).getInfect()+"��";
						if(sp==4)
							s+=" ���ƻ���"+a.get(i).getDoubt()+"��";
						if(cure==4)
							s+=" ����"+a.get(i).getHealth()+"��";
						if(dead==4)
							s+=" ����"+a.get(i).getDeath()+"��";
					}
					
					output.println(s);
				}
			}
			output.println("// ���ĵ�������ʵ���ݣ���������ʹ��");
		}
		 catch (IOException e) 
		 {
	         e.printStackTrace();
	     }
	}
}
