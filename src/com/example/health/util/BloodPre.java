package com.example.health.util;

public class BloodPre {
	    private String userid;  
	    private String time;
	    private int highp;  
	    private int lowp;
	    private int pulse;
	      
	    public BloodPre(){
	    	
	    }

		public String getUserid() {
			return userid;
		}

		public void setUserid(String userid) {
			this.userid = userid;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public int getHighp() {
			return highp;
		}

		public void setHighp(int highp) {
			this.highp = highp;
		}

		public int getLowp() {
			return lowp;
		}

		public void setLowp(int lowp) {
			this.lowp = lowp;
		}

		public int getPulse() {
			return pulse;
		}

		public void setPulse(int pulse) {
			this.pulse = pulse;
		}
		
		@Override
		public String toString() {
			// TODO Auto-generated method stub
//			return "BloodPre [userid=" + userid + ", time=" + time + ", highp=" + highp + ", lowp=" + lowp + ", pulse=" + pulse + "]";
			return "\'"+userid+"\',\'"+time+"\',\'"+highp+"\',\'"+lowp+"\',\'"+pulse+"\'";
		}
	   
}

