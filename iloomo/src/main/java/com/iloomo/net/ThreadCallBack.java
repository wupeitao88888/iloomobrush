package com.iloomo.net;

import java.io.Serializable;

public interface ThreadCallBack extends Serializable {
	public  void onCallbackFromThread(String resultJson, Object modelClass);
	public void onCallBackFromThread(String resultJson, int resultCode, Object modelClass);

	public  void onCallbackFromThreadError(String resultJson, Object modelClass);
	public void onCallBackFromThreadError(String resultJson, int resultCode, Object modelClass);

}
