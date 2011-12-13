package tkbEntities;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
public class DataAccess {
	
	private static final String recordName = "tkbRMS";
	public static final void writeRecord(String day,String mor,String ord,String content){
		  tkbEntities tkb = new tkbEntities();
		  tkb.setDay(day);
			tkb.setSC(mor);
			tkb.setOrd(ord);
			tkb.setContent(content);
			addRecord(tkb);
	  }
	  public static final String getbyDayReturnString(String d){
		  tkbEntities[] listTKB=  DataAccess.getList();
		  String ret="Thứ "+d; 
		  ret+="\nSáng:\n";
		  for(int j=1;j<=5;j++)
		  for (int i =listTKB.length-1;i>=0; i--)			  
		  if(listTKB[i].getDay().equals(d) && listTKB[i].getSC().equals("1")
				  && listTKB[i].getOrd().equals(Integer.toString(j))) // morning
		  {  
			  ret+="Tiết "+listTKB[i].getOrd()+" : " + listTKB[i].getContent()+"\n";
		  }	
		  ret+="Chiều:\n";
		  for(int j=1;j<=5;j++)
		  for (int i =listTKB.length-1;i>=0; i--)	
		  if(listTKB[i].getDay().equals(d)&& listTKB[i].getSC().equals("2")
				  && listTKB[i].getOrd().equals(Integer.toString(j))) // morning
		  {  
			  ret+="Tiết "+listTKB[i].getOrd()+" : " + listTKB[i].getContent()+"\n";
		  }
		return ret;
	    
	  }
	  public static final tkbEntities[] getbyDayReturnArray(String d){
		  tkbEntities[] listTKB=  DataAccess.getList();
		  		   
		  
		  int no=0;
		  for (int i =listTKB.length-1;i>=0; i--)			  
			  if(listTKB[i].getDay().equals(d)) // 
			  {  
				  no++;
			  }
		  tkbEntities[] ret=new tkbEntities[no] ;
		  int k=0;
		  for (int i =listTKB.length-1;i>=0; i--)			  
		  if(listTKB[i].getDay().equals(d)) // 
		  {  
			  ret[k]=listTKB[i];
			  k++;
		  }
		return ret;
	    
	  }
	public static final synchronized tkbEntities[] getList() {
		tkbEntities[] results = null;
		tkbEntities result = null;
		RecordStore rec = null;
		DataInputStream dis = null;
		try {
			rec = RecordStore.openRecordStore(recordName, true);
			int numberRecord = rec.getNumRecords();
			if(numberRecord==0){
				// save init
				for(int d=1;d<=7;d++)
					for(int sc=1;sc<=2;sc++)
						for(int tiet=1;tiet<=5;tiet++)												
							writeRecord(Integer.toString(d),Integer.toString(sc),Integer.toString(tiet),"");						
				rec = RecordStore.openRecordStore(recordName, true);
				numberRecord = rec.getNumRecords();
			}
			
			results = new tkbEntities[numberRecord];
			RecordEnumeration re = rec.enumerateRecords(null, null, false);
			int i = 0;
			while (re.hasNextElement()) {
				// Get next record
				dis = new DataInputStream(new ByteArrayInputStream(re
						.nextRecord()));
				result = new tkbEntities();
				result.setDay(dis.readUTF());
				result.setSC(dis.readUTF());
				result.setOrd(dis.readUTF());
				result.setContent(dis.readUTF());
				results[i] = result;
				i++;
			}

		} catch (Exception ex) {
			
			results = null;
		} finally {
			if (rec != null) {
				try {
					rec.closeRecordStore();
				} catch (Exception e) {
					results = null;
				}
				rec = null;
			}
			if (dis != null) {
				try {
					dis.close();
				} catch (Exception e) {
				}
				rec = null;
			}
		}
		return results;
	}

	public static synchronized void addRecord(tkbEntities tkbEntities) {
		RecordStore rec = null;
		ByteArrayOutputStream bos = null;
		DataOutputStream dos = null;
		try {
			// deleteRecord(recordName);
			bos = new ByteArrayOutputStream();
			dos = new DataOutputStream(bos);
			dos.writeUTF(tkbEntities.getDay());
			dos.writeUTF(tkbEntities.getSC());
			dos.writeUTF(tkbEntities.getOrd());
			dos.writeUTF(tkbEntities.getContent());
			dos.flush();
			rec = RecordStore.openRecordStore(recordName, true);
			byte[] b = bos.toByteArray();
			rec.addRecord(b, 0, b.length);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (rec != null) {
				try {
					rec.closeRecordStore();
				} catch (Exception e) {
				}
				rec = null;
			}
			if (dos != null) {
				try {
					dos.close();
				} catch (IOException ex) {
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException ex) {
				}
			}
		}
	}

	public static final synchronized void updateRecord(String day,String mor,String ord,String content) {
		RecordStore rec = null;
		DataInputStream dis = null;
		ByteArrayOutputStream bos = null;
		DataOutputStream dos = null;
		tkbEntities result = null;
		try {

			rec = RecordStore.openRecordStore(recordName, true);
			RecordEnumeration re = rec.enumerateRecords(null, null, false);

			while (re.hasNextElement()) {
				int idx = re.nextRecordId();

				// Get next record
				dis = new DataInputStream(new ByteArrayInputStream(rec
						.getRecord(idx)));
				// get record to object
				result = new tkbEntities();
				result.setDay(dis.readUTF());
				result.setSC(dis.readUTF());
				result.setOrd(dis.readUTF());
				result.setContent(dis.readUTF());
				
				if (result.getDay().equals(day) &&
						result.getSC().equals(mor) &&
						result.getOrd().equals(ord)) {
					bos = new ByteArrayOutputStream();
					dos = new DataOutputStream(bos);
					dos.writeUTF(day);
					dos.writeUTF(mor);
					dos.writeUTF(ord);
					dos.writeUTF(content);
					dos.flush();

					byte[] b = bos.toByteArray();
					rec.setRecord(idx, b, 0, b.length);
					break;
				}

			}

		} catch (Exception ex) {
			

		} finally {
			if (rec != null) {
				try {
					rec.closeRecordStore();
				} catch (Exception e) {

				}
				rec = null;
			}
			if (dis != null) {
				try {
					dis.close();
				} catch (Exception e) {
				}
				rec = null;
			}
		}

	}
}
