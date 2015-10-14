package com.controller;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.model.ReadXlsx;
import com.model.WriteInvo1;
import com.model.WriteInvo2;
import com.model.WriteXlsx;
import com.model.CreateColumn;

public class MainClass {
public static void main(String args[]) throws IOException
{
	int iCount=0,uCount=0,dCount=0;
	List<String> sqlDML=new ArrayList<String>();
	List<String> typeSQL=new ArrayList<String>();
	List<String>targetTable= new ArrayList<String>();
	List<String>targetDB= new ArrayList<String>();
	List<String>sqlId=new ArrayList<String>();
	ReadXlsx readfile= new ReadXlsx();
	String[] sID = null;
	String ID = null;
	sqlDML=readfile.GetXlsx();
	int id=1;
	WriteXlsx.WriteCol("Type of SQL Statement","SQL Identifier","Target Schema / Database","Target Table","Target Column","Source Schema / Database","Source Table","Source Column","Transformation");
	//WriteInvo1.WriteCol("QueryType", "Number of Queries");
	WriteInvo2.WriteCol("Query Number","Type Of query","Number of words","Number of Target Columns");
	for(String sql:sqlDML)
	{
		if(sql.matches("-?\\d+(\\.\\d+)?"))
				{
				sqlId.add(sql);
				System.out.println(sql);
				ID=sql;
				id++;
				}
		else if(sql.contains("Sql Identifier")||sql.contains("SQL Text"))
		{
			
		}
		else
		{
			String Value1=CreateColumn.columnOne(sql);
			typeSQL.add(Value1);
			String value2=CreateColumn.columnTwo(Value1, sql,ID);
			String[] values= value2.split("\\.");
			targetTable.add(values[1]);
			targetDB.add(values[0]);
			//System.out.println(values[0]+": "+values[1]);
		}
		
	}
	for(String typeSql:typeSQL)
	{
		if(typeSql.toLowerCase().contains("insert"))
		{
			iCount++;
		}
		if(typeSql.toLowerCase().contains("update"))
		{
			uCount++;
		}
		if(typeSql.toLowerCase().contains("delete"))
		{
			dCount++;
		}
	}
	//WriteInvo1.WriteCol("QueryType", "Number of Queries");
	//WriteInvo1.WriteCol("Insert", Integer.toString(iCount));
	//WriteInvo1.WriteCol("Delete", Integer.toString(dCount));
	//WriteInvo1.WriteCol("Update", Integer.toString(uCount));
}
}
