package com.iloomo.db;


public class DButils {

	public DButils(DbHelperBase DbHelperBase) {
		super();
		DatabaseManager.initializeInstance(DbHelperBase);
	}


//	// ����б����
//	public synchronized void add(VisitorInfo nlistmsg) {
//		SQLiteDatabase writableDatabase = DatabaseManager.getInstance()
//				.openDatabase();
//		writableDatabase
//				.execSQL(
//						"insert into top(kfuserid,sex,province,city,status,updaterecenttime,isreport,userid,param6,usertype,iscrm,allocation,starcount,useridcrm,staytimelong,returncount,coupon,sessionstate,stat_buycount,stat_count,stat_lasttime,stat_lastkfname,username,saleindex,level,logintime,relavancy,count,time,lastkfname,age,credit,video,source,isdelete)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
//						new Object[] { nlistmsg.getKfuserid(),
//								nlistmsg.getSex(), nlistmsg.getProvince(),
//								nlistmsg.getCity(), nlistmsg.getStatus(),
//								nlistmsg.getUpdaterecenttime(),
//								nlistmsg.getIsreport(), nlistmsg.getUserid(),
//								nlistmsg.getParam6(), nlistmsg.getUsertype(),
//								nlistmsg.getIscrm(), nlistmsg.getAllocation(),
//								nlistmsg.getStarcount(),
//								nlistmsg.getUseridcrm(),
//								nlistmsg.getStaytimelong(),
//								nlistmsg.getReturncount(),
//								nlistmsg.getCoupon(),
//								nlistmsg.getSessionstate(),
//								nlistmsg.getStat_buycount(),
//								nlistmsg.getStat_count(),
//								nlistmsg.getStat_lasttime(),
//								nlistmsg.getStat_lastkfname(),
//								nlistmsg.getUsername(),
//								nlistmsg.getSaleindex(), nlistmsg.getLevel(),
//								nlistmsg.getLogintime(),
//								nlistmsg.getRelavancy(), nlistmsg.getCount(),
//								nlistmsg.getTime(), nlistmsg.getLastkfname(),
//								nlistmsg.getAge(), nlistmsg.getCredit(),
//								nlistmsg.getVideo(), nlistmsg.getSource(),
//								"false" });
//	}
//
//	// ��ѯ�б?�����
//	public synchronized List<VisitorInfo> select(String kfid) {
//		SQLiteDatabase readableDatabase = DatabaseManager.getInstance()
//				.readDatabase();
//		Cursor cursor = readableDatabase.rawQuery(
//				"select * from top where kfuserid=? and isdelete=?",
//				new String[] { kfid, "false" });
//		List<VisitorInfo> list = new ArrayList<VisitorInfo>();
//		while (cursor.moveToNext()) {
//			String kfuserid = cursor.getString(cursor
//					.getColumnIndex("kfuserid"));
//			String sex = cursor.getString(cursor.getColumnIndex("sex"));
//			String province = cursor.getString(cursor
//					.getColumnIndex("province"));
//			String city = cursor.getString(cursor.getColumnIndex("city"));
//			String status = cursor.getString(cursor.getColumnIndex("status"));
//			String updaterecenttime = cursor.getString(cursor
//					.getColumnIndex("updaterecenttime"));
//			String isreport = cursor.getString(cursor
//					.getColumnIndex("isreport"));
//			String userid = cursor.getString(cursor.getColumnIndex("userid"));
//			String param6 = cursor.getString(cursor.getColumnIndex("param6"));
//			String usertype = cursor.getString(cursor
//					.getColumnIndex("usertype"));
//			String iscrm = cursor.getString(cursor.getColumnIndex("usertype"));
//			String allocation = cursor.getString(cursor
//					.getColumnIndex("allocation"));
//			String starcount = cursor.getString(cursor
//					.getColumnIndex("starcount"));
//			String useridcrm = cursor.getString(cursor
//					.getColumnIndex("useridcrm"));
//			String staytimelong = cursor.getString(cursor
//					.getColumnIndex("staytimelong"));
//			String returncount = cursor.getString(cursor
//					.getColumnIndex("returncount"));
//			String coupon = cursor.getString(cursor.getColumnIndex("coupon"));
//			String sessionstate = cursor.getString(cursor
//					.getColumnIndex("sessionstate"));
//			String stat_buycount = cursor.getString(cursor
//					.getColumnIndex("stat_buycount"));
//			String stat_count = cursor.getString(cursor
//					.getColumnIndex("stat_count"));
//			String stat_lasttime = cursor.getString(cursor
//					.getColumnIndex("stat_lasttime"));
//			String stat_lastkfname = cursor.getString(cursor
//					.getColumnIndex("stat_lastkfname"));
//			String username = cursor.getString(cursor
//					.getColumnIndex("username"));
//			String saleindex = cursor.getString(cursor
//					.getColumnIndex("saleindex"));
//			String level = cursor.getString(cursor.getColumnIndex("level"));
//			String logintime = cursor.getString(cursor
//					.getColumnIndex("logintime"));
//			String relavancy = cursor.getString(cursor
//					.getColumnIndex("relavancy"));
//			String count = cursor.getString(cursor.getColumnIndex("count"));
//			String time = cursor.getString(cursor.getColumnIndex("time"));
//			String lastkfname = cursor.getString(cursor
//					.getColumnIndex("lastkfname"));
//			String age = cursor.getString(cursor.getColumnIndex("age"));
//			String credit = cursor.getString(cursor.getColumnIndex("credit"));
//			String video = cursor.getString(cursor.getColumnIndex("video"));
//			String source = cursor.getString(cursor.getColumnIndex("source"));
//			VisitorInfo vi = new VisitorInfo(kfuserid, sex, province, city,
//					status, updaterecenttime, isreport, userid, param6,
//					usertype, iscrm, allocation, starcount, useridcrm,
//					staytimelong, returncount, coupon, sessionstate,
//					stat_buycount, stat_count, stat_lasttime, stat_lastkfname,
//					username, saleindex, level, logintime, relavancy, count,
//					time, lastkfname, age, credit, video, source);
//			list.add(vi);
//		}
//		// DatabaseManager.getInstance().closeDatabase();
//		cursor.close();
//		return list;
//	}
//
//	// �޸ı��ص�ɾ��
//	public synchronized void updateDeleteUser(String userid, String kfuserid) {
//		SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
//		ContentValues values = new ContentValues();
//		values.put("isdelete", "true");// keyΪ�ֶ���valueΪֵ
//		db.update("top", values, "userid=? and kfuserid=?", new String[] {
//				userid, kfuserid });
//		// DatabaseManager.getInstance().closeDatabase();
//	}
//
//	public synchronized int selecttotal(String uid, String kfid) {
//
//		SQLiteDatabase readableDatabase = DatabaseManager.getInstance()
//				.readDatabase();
//		Cursor cursor = readableDatabase.rawQuery(
//				"select * from Contrasted where userid=?  and kfuserid=?",
//				new String[] { uid, kfid });
//
//		Log.e("����", uid + "::" + kfid);
//
//		int total = 0;
//		while (cursor.moveToFirst()) {
//			total = cursor.getInt(cursor.getColumnIndex("total"));
//			Log.e("����", total + "");
//			return total;
//		}
//		// DatabaseManager.getInstance().closeDatabase();
//		cursor.close();
//		return total;
//	}
//
//	public synchronized boolean checked(String sid, String kfid) {
//
//		SQLiteDatabase readableDatabase = DatabaseManager.getInstance()
//				.readDatabase();
//		Cursor cursor = readableDatabase.rawQuery(
//				"select * from Contrasted where userid=? and kfuserid=?",
//				new String[] { sid, kfid });
//
//		String customerid = null;
//		boolean b = true;
//		while (cursor.moveToLast()) {
//
//			customerid = cursor.getString(cursor.getColumnIndex("userid"));
//
//			if (customerid != null) {
//				b = false;
//				break;
//			} else {
//				b = true;
//				break;
//			}
//		}
//		// DatabaseManager.getInstance().closeDatabase();
//		cursor.close();
//		return b;
//	}
//
//	public synchronized int selectP(String userid, String kfid) {
//
//		SQLiteDatabase readableDatabase = DatabaseManager.getInstance()
//				.readDatabase();
//		Cursor cursor = readableDatabase.rawQuery(
//				"select * from Contrasted where userid=? and kfuserid=?",
//				new String[] { userid, kfid });
//
//		int position;
//		while (cursor.moveToLast()) {
//
//			position = cursor.getInt(cursor.getColumnIndex("position"));
//
//			return position;
//		}
//		// DatabaseManager.getInstance().closeDatabase();
//		cursor.close();
//		return 0;
//	}
//
//	public synchronized int delete(String userid, String kfid) {
//		SQLiteDatabase writeableDatabase = DatabaseManager.getInstance()
//				.openDatabase();
//		String whereClause = "userid=? and kfuserid=?";
//		// ɾ����������
//		String[] whereArgs = { String.valueOf(userid), String.valueOf(kfid) };
//		// ִ��ɾ��
//		int delete = writeableDatabase.delete("Contrasted", whereClause,
//				whereArgs);
//		return delete;
//	}
//
//	public synchronized void update(VisitorList n) {
//		SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
//		ContentValues values = new ContentValues();
//		values.put("lastmsg", n.getLastmsg());// keyΪ�ֶ���valueΪֵ
//		values.put("time", n.getTime());// keyΪ�ֶ���valueΪֵ
//		values.put("type", n.getType());// keyΪ�ֶ���valueΪֵ
//		values.put("total", n.getTotal());// keyΪ�ֶ���valueΪֵ
//		values.put("status", n.getStatus());// keyΪ�ֶ���valueΪֵ
//		db.update("Contrasted", values, "userid=? and kfuserid=?",
//				new String[] { n.getUserid(), n.getKfuserid() });
//		// DatabaseManager.getInstance().closeDatabase();
//	}
//
//	// �޸ĵ�����Ϣ����
//	public void updatet(VisitorList n) {
//		SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
//		ContentValues values = new ContentValues();
//		values.put("total", 0);// keyΪ�ֶ���valueΪֵ
//		db.update("Contrasted", values, "userid=? and kfuserid=?",
//				new String[] { n.getUserid(), n.getKfuserid() });
//		// DatabaseManager.getInstance().closeDatabase();
//	}
//
//	// �޸�״̬
//	public synchronized void updatetstatus(VisitorList n) {
//		SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
//		ContentValues values = new ContentValues();
//		values.put("status", n.getStatus());// keyΪ�ֶ���valueΪֵ
//		db.update("Contrasted", values, "userid=? and kfuserid=?",
//				new String[] { n.getUserid(), n.getKfuserid() });
//		// DatabaseManager.getInstance().closeDatabase();
//	}
//
//	public synchronized void updateCRM(String newname, String province,
//			String city, String userid, String kfuserid) {
//		SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
//		ContentValues values = new ContentValues();
//		values.put("username", newname);// keyΪ�ֶ���valueΪֵ
//		values.put("province", newname);// keyΪ�ֶ���valueΪֵ
//		values.put("city", newname);// keyΪ�ֶ���valueΪֵ
//		db.update("Contrasted", values, "userid=? and kfuserid=?",
//				new String[] { userid, kfuserid });
//		// DatabaseManager.getInstance().closeDatabase();
//	}
//
//	// ����������ļ�¼����Ϣ
//	public synchronized void addchat(ChatMsgEntity entity) {
//		SQLiteDatabase writableDatabase = DatabaseManager.getInstance()
//				.openDatabase();
//		writableDatabase
//				.execSQL(
//						"insert into chatinfo(customerid,customername,messageid,customericon,type,content,time,isComMeg,kfuserid,length,size,title,chatid,url,remark,sendstatus,location)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
//						new Object[] { entity.getCustomerId(),
//								entity.getCustomerName(),
//								entity.getMessageId(),
//								entity.getCustomerIcon(), entity.getType(),
//								entity.getContent(), entity.getTime(),
//								entity.getIsComMeg(), entity.getKfuserid(),
//								entity.getLength(), entity.getSize(),
//								entity.getTitle(), entity.getChatid(),
//								entity.getUrl(), entity.getRemark(),
//								entity.getSendstatus(), entity.getLocation() });
//		// DatabaseManager.getInstance().closeDatabase();
//	}
//
//	public synchronized List<ChatMsgEntity> selectchat(String chatid,
//			String kfuserid) {
//		SQLiteDatabase readableDatabase = DatabaseManager.getInstance()
//				.readDatabase();
//		Cursor cursor = readableDatabase
//				.rawQuery(
//						// "select * from chatinfo where chatid=? and kfuserid=? order by id desc limit 10",
//						"select * from chatinfo where chatid=? and kfuserid=? order by time asc",
//						new String[] { chatid, kfuserid });
//		List<ChatMsgEntity> list = new ArrayList<ChatMsgEntity>();
//		while (cursor.moveToNext()) {
//			String customerid = cursor.getString(cursor
//					.getColumnIndex("customerid"));
//			String customername = cursor.getString(cursor
//					.getColumnIndex("customername"));
//			String messageid = cursor.getString(cursor
//					.getColumnIndex("messageid"));
//			String customericon = cursor.getString(cursor
//					.getColumnIndex("customericon"));
//			String type = cursor.getString(cursor.getColumnIndex("type"));
//
//			String content = cursor.getString(cursor.getColumnIndex("content"));
//			String time = cursor.getString(cursor.getColumnIndex("time"));
//			String kfid = cursor.getString(cursor.getColumnIndex("kfuserid"));
//			String isComMeg = cursor.getString(cursor
//					.getColumnIndex("isComMeg"));
//			String length = cursor.getString(cursor.getColumnIndex("length"));
//			String size = cursor.getString(cursor.getColumnIndex("size"));
//			String title = cursor.getString(cursor.getColumnIndex("title"));
//			String cid = cursor.getString(cursor.getColumnIndex("chatid"));
//			String url = cursor.getString(cursor.getColumnIndex("url"));
//			String remark = cursor.getString(cursor.getColumnIndex("remark"));
//			String sendstatus = cursor.getString(cursor
//					.getColumnIndex("sendstatus"));
//			String location = cursor.getString(cursor
//					.getColumnIndex("location"));
//			ChatMsgEntity entity = null;
//			if (!"".equals(isComMeg)) {
//
//				entity = new ChatMsgEntity(messageid, customerid, customericon,
//						customername, type, content, time, isComMeg);
//				entity.setKfuserid(kfid);
//				entity.setLength(length);
//				entity.setSize(size);
//				entity.setTitle(title);
//				entity.setChatid(cid);
//				entity.setUrl(url);
//				entity.setRemark(remark);
//				entity.setSendstatus(sendstatus);
//				entity.setLocation(location);
//			}
//			list.add(entity);
//		}
//		// DatabaseManager.getInstance().closeDatabase();
//		cursor.close();
//		return list;
//	}
//
//	public synchronized boolean chatchecked(String cid, String kfid, String mid) {
//
//		SQLiteDatabase readableDatabase = DatabaseManager.getInstance()
//				.readDatabase();
//		Cursor cursor = readableDatabase
//				.rawQuery(
//						"select * from chatinfo where chatid=? and kfuserid=? and messageid=?",
//						new String[] { cid, kfid, mid });
//
//		String customerid = null;
//		boolean b = true;
//		while (cursor.moveToLast()) {
//
//			customerid = cursor.getString(cursor.getColumnIndex("messageid"));
//
//			if (customerid != null) {
//				b = false;
//				break;
//			} else {
//				b = true;
//				break;
//			}
//		}
//		// DatabaseManager.getInstance().closeDatabase();
//		cursor.close();
//
//		Log.e("��������Ϣ", "" + b);
//		return b;
//	}
//
//	public synchronized void updatemsg(String userid, String kfuserid,
//			String messageid) {
//		SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
//		ContentValues values = new ContentValues();
//		values.put("sendstatus", "1");// keyΪ�ֶ���valueΪֵ
//		db.update("chatinfo", values,
//				"chatid=? and kfuserid=? and messageid=?", new String[] {
//						userid, kfuserid, messageid });
//		// DatabaseManager.getInstance().closeDatabase();
//	}

}
