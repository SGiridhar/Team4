package com.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UpdateMapping {
	public static int upd;

	public static void getUMapping(String sql2, String result1, String ID) throws IOException {
		upd = 0;
		String result = "no";
		List<String> sourceCol = new ArrayList();
		List<String> desCol = new ArrayList();
		int sIndex = -1, eIndex = -1;
		String query = "";
		String[] sql = sql2.split("\\s+");

		for (int j = 0; j < sql.length; j++) {
			if (sql[j].toLowerCase().contains("set")) {
				sIndex = j + 1;
				result = "yes";
				// System.out.println("result is yes");
			}
			// if(result=="yes")
			// {
			// if(sql[j].contains("="))
			// {
			// //sourceCol.add(sql[j-1]);
			// System.out.println(sql[j-1]);
			// System.out.println(sql[j]);
			// System.out.println(sql[j+1]);
			// //desCol.add(checkcol(sql[j+1],sql));
			//
			// }
			// }
			if (sql[j].toLowerCase().contains("where") && j > sIndex && sIndex != -1) {
				eIndex = j - 1;
				break;
			}
		}

		for (int i1 = sIndex; i1 <= eIndex; i1++) {
			query = query + sql[i1];
		}
		String[] val = query.split(",");

		// System.out.println(query);
		String[] val1 = null;
		int x = 0, j = 0;
		String[] values = result1.split("\\.");
		for (int i = 0; i < val.length; i++) {
			if (val[i].toLowerCase().contains("=")) {

				String val3[] = val[i].split("=");
				desCol.add(val3[0]);

				String sCol = checkcol(val3[1], sql);
				sourceCol.add(sCol);
				// System.out.println(val3[0]+" ="+val3[1]);
				if (sCol.toLowerCase().contains(",")) {
					if (sCol.contains("\\$")) {
						String[] nwjoin = sCol.split("\\$");
						sCol = nwjoin[0];
						String[] Sinfo1 = sCol.split("\\,");
						System.out.println(nwjoin[0] + "....." + nwjoin[1]);
						for (int w = 0; w < Sinfo1.length; w++) {
							
							if(Sinfo1.length == 3 && w<nwjoin.length){
							String[] Sinfo = Sinfo1[w].split("\\.");
							nwjoin[w+1] = nwjoin[w+1].replace("\\","");
							WriteXlsx.WriteCol("Update", ID, values[0], values[1], val3[0], Sinfo[0], Sinfo[1], Sinfo[2],
									"JOIN"+" "+nwjoin[w+1]);
						}}
						upd++;
					} else {
					String[] Sinfo1 = sCol.split("\\,");
					for (int w = 0; w < Sinfo1.length; w++) {
						String[] Sinfo = Sinfo1[w].split("\\.");
						WriteXlsx.WriteCol("Update", ID, values[0], values[1], val3[0], Sinfo[0], Sinfo[1], Sinfo[2],
								"NIL");
					
					}
					upd++;
					}
				} else {
					if (sCol != "" && sCol.split("\\.").length == 3) {
						String[] Sinfo2 = sCol.split("\\.");
						System.out.println(sCol);
						System.out.println(Sinfo2.length);
						System.out.println(Sinfo2[2]);
						WriteXlsx.WriteCol("Update", ID, values[0], values[1], val3[0], Sinfo2[0], Sinfo2[1], Sinfo2[2],
								"NIL");
						upd++;
					} else if (sCol.toUpperCase().contains("CURRENT_TIMESTAMP(0)")
							|| sCol.toUpperCase().contains("CTRL")) {
						WriteXlsx.WriteCol("Update", ID, values[0], values[1], val3[0], "NA", "NA", sCol, "NIL");
						upd++;
					}
				}
				x++;
				x++;
			}
			// System.out.println(val[i]);
		}

	}

	public static String checkcol(String result, String[] sql) {
		String colName = "";
		String col1 = "";
		String col2 = "";
		int pointer = 10;
		String joincond = "";
		if (result.indexOf(".") != -1) {
			String[] values = result.split("\\.");

			for (int i = 0; i < sql.length; i++) {
				if (sql[i].contains(values[0])) {
					if (sql[i - 1].indexOf(".") != -1) {
						sql[i - 1] = sql[i - 1].replace(",", "");
						colName = sql[i - 1] + "." + values[1];
						// System.out.println(colName);
						// System.out.println(sql[i-1]);
					} else if (sql[i].contains(")") || sql[i - 1].contains(")")) {
						for (int m = i; m >= 1; m--) {
							if (sql[m].toLowerCase().contains("from")) {
								if (sql[m + 1].indexOf(".") != -1) {
									sql[m + 1].replace(",", "");
									col1 = sql[m + 1] + "." + values[1];
									// System.out.println(col1);

									// System.out.println(sql[i-1]);
								}
							}
							if (sql[m].toLowerCase().contains("join")) {
								sql[m + 1].replace(",", "");
								col2 = sql[m + 1] + "." + values[1];
								for (int adv = m + 1; adv < sql.length; adv++) {
									if (sql[adv].toLowerCase().contains("on")) {
										pointer = 1;
										joincond = joincond + "\\$";
									}
									if (sql[adv].toLowerCase().contains("left")
											|| sql[adv].toLowerCase().contains("right")
											|| sql[adv].toLowerCase().contains("join")|| sql[adv].toLowerCase().contains("where")) {

										pointer = 0;
										break;
									}
									if (pointer == 1) {
										joincond = joincond + " " + sql[adv];
									}
								}
								// System.out.println(col2);
							}
							colName = col1 + "," + col2+joincond;
						}
					}
				}
			}
		}
		if (result.toUpperCase().contains("CURRENT_TIMESTAMP(0)") || result.toUpperCase().contains("CTRL")) {
			colName = result;
		}
		System.out.println(colName);
		return colName;

	}
}
