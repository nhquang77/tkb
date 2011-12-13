import java.util.Calendar;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import tkbEntities.DataAccess;
import tkbEntities.tkbEntities;


public class tkb extends MIDlet implements CommandListener{
	/*-------------------- all forms ----------------------*/
	private Display display;
	private Form formShow = new Form("THỜI KHÓA BIỂU");
	private Form formEdit = new Form("Sửa TKB");
	private Form formNotify = new Form("");	
	private List listDay;
	private List listMenu;
	/*-----------------------------------------------------*/
	/*-------------------- options ----------------------*/
	private int  mode = List.IMPLICIT;
	public static final String[] items = {
	        "Thứ hai", "Thứ ba", "Thứ tư", "Thứ năm","Thứ sáu","Thứ bảy","Chủ nhật"
	    };
	public static final String[] itemMenus = {
        "Xem theo ngày ", "Sửa theo ngày","Tác giả"
    };
	/*-----------------------------------------------------*/
	/*-------------------- items ----------------------*/	
	private StringItem content ;
	private Command menu = new Command("Menu", Command.SCREEN, 1);
	
	private Command exitApp = new Command("Thoát", Command.EXIT, 1);
	private Command exitDays = new Command("Trở về", Command.EXIT, 1);
	private Command exitEdit = new Command("Hủy bỏ", Command.EXIT, 1);
	
	private Command chooseDays = new Command("Chọn", Command.SCREEN, 1);
	private Command saveEdit = new Command("Lưu", Command.SCREEN, 1);
	private Command okNotify = new Command("Đồng ý", Command.SCREEN, 1);
	private Command chooseMenu=new Command("Chọn",Command.SCREEN,1);
	private Command exitMenu=new Command("Trở về",Command.EXIT,1);
	
	
	private TextField t1 = new TextField("Tiết 1:", "", 30, TextField.ANY);
	private TextField t2 = new TextField("Tiết 2:", "", 30, TextField.ANY);
	private TextField t3 = new TextField("Tiết 3:", "", 30, TextField.ANY);
	private TextField t4 = new TextField("Tiết 4:", "", 30, TextField.ANY);
	private TextField t5 = new TextField("Tiết 5:", "", 30, TextField.ANY);
	private TextField t6 = new TextField("Tiết 1:", "", 30, TextField.ANY);
	private TextField t7 = new TextField("Tiết 2:", "", 30, TextField.ANY);
	private TextField t8 = new TextField("Tiết 3:", "", 30, TextField.ANY);
	private TextField t9 = new TextField("Tiết 4:", "", 30, TextField.ANY);
	private TextField t10 = new TextField("Tiết 5:", "", 30, TextField.ANY);
	
	/*-----------------------------------------------------*/
	/*-------------------- global variables ----------------------*/
	private String day;	 // day to View or Edit
	private int action; //1 : view tkb by date, 2 : edit tkb by date
	/*-----------------------------------------------------*/
	
	
	public tkb() {
		// TODO Auto-generated constructor stub
		display = Display.getDisplay(this);		
		
		formShow.addCommand(exitApp);
		formShow.addCommand(menu);
		getTKB(false);
		
		formShow.setCommandListener(this);
		listMenu=new List("",mode,itemMenus,null);
		listMenu.addCommand(exitMenu);
		listMenu.addCommand(chooseMenu);
		listMenu.setCommandListener(this);
		
		listDay=new List("",mode,items,null);
		listDay.addCommand(chooseDays);
		listDay.addCommand(exitDays);
		listDay.setCommandListener(this);
		formEdit.append(new StringItem("Sáng",null));
		formEdit.append(t1);
		formEdit.append(t2);
		formEdit.append(t3);
		formEdit.append(t4);
		formEdit.append(t5);
		formEdit.append(new StringItem("Chiều",null));
		formEdit.append(t6);
		formEdit.append(t7);
		formEdit.append(t8);
		formEdit.append(t9);
		formEdit.append(t10);
		formEdit.addCommand(saveEdit);
		formEdit.addCommand(exitEdit);
		formEdit.setCommandListener(this);
		formNotify.addCommand(okNotify);
		formNotify.setCommandListener(this);
		
	}
	public void getTKB(boolean byDate){	
		formShow.deleteAll();
		
		if(!byDate) day=getDayOfWeek();
		else{
			int idx=listDay.getSelectedIndex()+2;
			if(idx>=8) idx=1;
			day=Integer.toString(idx);
		}
		content= new StringItem(null, DataAccess.getbyDayReturnString(day));
		formShow.append(content);
		display.setCurrent(formShow);
	}
	public void showNotify(String msg){
		formNotify.append(new StringItem(msg,null));
		display.setCurrent(formNotify);
		
	}
	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		// TODO Auto-generated method stub

	}

	protected void pauseApp() {
		// TODO Auto-generated method stub

	}

	protected void startApp() throws MIDletStateChangeException {
		// TODO Auto-generated method stub
		display.setCurrent(formShow);
	}

	public void commandAction(Command command, Displayable displayable) {
		// TODO Auto-generated method stub
		if (command == menu) {			 
				display.setCurrent(listMenu);		     
		    } 
		else if (command == exitApp) {
		    try {
				destroyApp(false);
			} catch (MIDletStateChangeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		      notifyDestroyed();
		}
		else if (command == exitDays) {
			getTKB(true);
		}
		else if (command == chooseDays) {
			int idx=listDay.getSelectedIndex()+2;
			if(idx>=8) idx=1;
			day=Integer.toString(idx);
			if(action==2){ // edit by date
			
			tkbEntities[] ret=DataAccess.getbyDayReturnArray(day);
			for(int i=0;i<ret.length;i++){
				if(ret[i].getSC().equals("1") && ret[i].getOrd().equals("1"))
					t1.setString(ret[i].getContent());
				else if(ret[i].getSC().equals("1") && ret[i].getOrd().equals("2"))
					t2.setString(ret[i].getContent());
				else if(ret[i].getSC().equals("1") && ret[i].getOrd().equals("3"))
					t3.setString(ret[i].getContent());
				else if(ret[i].getSC().equals("1") && ret[i].getOrd().equals("4"))
					t4.setString(ret[i].getContent());
				else if(ret[i].getSC().equals("1") && ret[i].getOrd().equals("5"))
					t5.setString(ret[i].getContent());
				else if(ret[i].getSC().equals("2") && ret[i].getOrd().equals("1"))
					t6.setString(ret[i].getContent());
				else if(ret[i].getSC().equals("2") && ret[i].getOrd().equals("2"))
					t7.setString(ret[i].getContent());
				else if(ret[i].getSC().equals("2") && ret[i].getOrd().equals("3"))
					t8.setString(ret[i].getContent());
				else if(ret[i].getSC().equals("2") && ret[i].getOrd().equals("4"))
					t9.setString(ret[i].getContent());
				else if(ret[i].getSC().equals("2") && ret[i].getOrd().equals("5"))
					t10.setString(ret[i].getContent());
			}
			display.setCurrent(formEdit);
			}
			else if(action ==1){ // view by date
				getTKB(true);
			}
		}
		else if (command == exitEdit) {
			display.setCurrent(listDay);
		}
		else if (command == saveEdit) {
			// save edit
			int idx=listDay.getSelectedIndex()+2;
			if(idx>=8) idx=1;
			day=Integer.toString(idx);
			//day=getDayOfWeek();
		
			DataAccess.updateRecord(day,"1","1",t1.getString());
			DataAccess.updateRecord(day,"1","2",t2.getString());
			DataAccess.updateRecord(day,"1","3",t3.getString());
			DataAccess.updateRecord(day,"1","4",t4.getString());
			DataAccess.updateRecord(day,"1","5",t5.getString());
			DataAccess.updateRecord(day,"2","1",t6.getString());
			DataAccess.updateRecord(day,"2","2",t7.getString());
			DataAccess.updateRecord(day,"2","3",t8.getString());
			DataAccess.updateRecord(day,"2","4",t9.getString());
			DataAccess.updateRecord(day,"2","5",t10.getString());
			showNotify("Lưu thành công!");
		}
		else if(command==okNotify){
			getTKB(true);
		}
		else if(command==chooseMenu){
			action=listMenu.getSelectedIndex()+1;
			if(action==3){
				showNotify("Tác giả : Trịnh Thanh Dũng \n Phiên bản : Tháng 9/2011 \n Di động : 01683078688 \n Email : trinhthanhdung@gmail.com");
			}else{
				display.setCurrent(listDay);
			}
		}
		else if(command==exitMenu){
			getTKB(true);
		}
		
	}
	
	/*--------- global functions --------------*/
	  
	  public static String getDayOfWeek() {
			Calendar c = Calendar.getInstance();
			
			int m = c.get(Calendar.DAY_OF_WEEK);

			return Integer.toString(m);
		}
	/*----------------------------*/
	  
}

