package com.model;

import java.io.IOException;

public class CreateColumn {

	public static String columnOne(String s)
	{
		String result="";
		if(s.toLowerCase().contains("insert"))
		{
			result="Insert";
		}
		else if(s.toLowerCase().contains("delete"))
		{
			result="Delete";
		}
		else if(s.toLowerCase().contains("update"))
		{
			result="Update";
		}
		else
		{
			result="Not a DML statement";
		}
		return result;
	}
	public static String columnTwo(String s,String sqlDML,String ID) throws IOException
	{
	
		String result="";
		String[] sql= sqlDML.split("\\s+");
//		for(int i=0; i<sql.length;i++)
//		{
//			System.out.println(sql[i]);
//		}
//		
		if(s.toLowerCase().contains("insert"))
		{
			//result=sql.substring(sql.toLowerCase().indexOf("into")+5,sql.toLowerCase().indexOf(")"));
			for(int j=0;j<sql.length;j++)
			{
				if(sql[j].toLowerCase().equals("into"))
				{
					result=CreateColumn.checkval(sql,sql[j+1]);
					InsertMapping.getIMapping(sqlDML,result,ID);
					
					
				}
			}
			WriteInvo2.WriteCol(ID,"Insert",Integer.toString(sqlDML.length()),Integer.toString(InsertMapping.ins));
			
		}
		else if(s.toLowerCase().contains("delete"))
		{
			//result=sql.substring(sql.toLowerCase().indexOf("from")+5,sql.toLowerCase().indexOf(" "));
			WriteInvo2.WriteCol(ID,"Delete",Integer.toString(sqlDML.length()),"NA");
			for(int j=0;j<sql.length;j++)
			{
				if(sql[j].toLowerCase().equals("from")&& sql[j-1].toLowerCase().equals("delete"))
				{
					result=CreateColumn.checkval(sql,sql[j+1]);
					DeleteMapping.getDMapping(sqlDML,result,ID);
					//System.out.println("result:"+result);
				
				}
			}
		}
		else if(s.toLowerCase().contains("update"))
		{
			//result=sql.substring(sql.toLowerCase().indexOf("update")+7,sql.toLowerCase().indexOf(" "));
			
			for(int j=0;j<sql.length;j++)
			{
				if(sql[j].toLowerCase().equals("update"))
				{
					
					result=CreateColumn.checkval(sql,sql[j+1]);
					UpdateMapping.getUMapping(sqlDML,result,ID);
					
					
				}
			}
			if(UpdateMapping.upd>0)
			{
			WriteInvo2.WriteCol(ID,"Update",Integer.toString(sqlDML.length()),Integer.toString(UpdateMapping.upd));
		}
			if(UpdateMapping.upd==0)
			{
				WriteInvo2.WriteCol(ID,"Update",Integer.toString(sqlDML.length()),"All Columns");
			}
		}
		else
		{
			result="Logical Error";
		}
	
		return result;
		
		
	}
	public static String checkval(String[] sql,String result)
	{
		String colName="";
		if(result.indexOf(".")!=-1)
				{
			colName=result;
				}
		else
		{
			
			for(int i=0; i<sql.length;i++)
			{	
				if(sql[i].contains(result))
				{
					if(sql[i-1].indexOf(".")!=-1)
					{
					  colName=sql[i-1];
					}
				}
			}
		}	
		
		return colName;
	}
}
