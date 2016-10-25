package com.iloomo.bean;



public class ZfbPayItem {
	 public String body;
     public String out_trade_no;
     public String subject;
     public String total_fee;
     public boolean isDingjin;
		public String getBody() {
			return body;
		}
		public void setBody(String body) {
			this.body = body;
		}
		public String getOut_trade_no() {
			return out_trade_no;
		}
		public void setOut_trade_no(String out_trade_no) {
			this.out_trade_no = out_trade_no;
		}
		public String getSubject() {
			return subject;
		}
		public void setSubject(String subject) {
			this.subject = subject;
		}
		public String getTotal_fee() {
			return total_fee;
		}
		public void setTotal_fee(String total_fee) {
			this.total_fee = total_fee;
		}
		public boolean isDingjin() {
			return isDingjin;
		}
		public void setDingjin(boolean isDingjin) {
			this.isDingjin = isDingjin;
		}
		public ZfbPayItem(String body, String out_trade_no, String subject,
				String total_fee, boolean isDingjin) {
			super();
			this.body = body;
			this.out_trade_no = out_trade_no;
			this.subject = subject;
			this.total_fee = total_fee;
			this.isDingjin = isDingjin;
		}
     
}
