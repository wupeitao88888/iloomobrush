package com.iloomo.vpn.util;

import com.iloomo.utils.L;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.StringBuilder;

public class RunCommand {
	public static Process run(String command) throws IOException {
		ProcessBuilder builder = new ProcessBuilder("su");
		Process p = builder.start();
		DataOutputStream dos = new DataOutputStream(p.getOutputStream());

		dos.writeBytes(command + "\n");
		dos.flush();
		dos.writeBytes("exit\n");
		dos.flush();
		return p;
	}
	public static int excuteSuCMD(String cmd) {
		String s = "\n";
		L.e("开始执行命令");
		try {
			Process process = Runtime.getRuntime().exec("su");
			DataOutputStream dos = new DataOutputStream((OutputStream) process.getOutputStream());
			// 部分手机Root之后Library path 丢失，导入path可解决该问题
			dos.writeBytes((String) "export LD_LIBRARY_PATH=/vendor/lib:/system/lib\n");
			cmd = String.valueOf(cmd);
			dos.writeBytes((String) (cmd + "\n"));
			dos.flush();
			dos.writeBytes("exit\n");
			dos.flush();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String line = null;
			while ((line = in.readLine()) != null) {
				s += line + "\n";
			}

			String error = null;
			DataInputStream ise = new DataInputStream(process.getErrorStream());

			while ((error = ise.readLine()) != null) {
				s += error + "\n";
			}
			L.e("--------------------"+s);
			process.waitFor();
			int result = process.exitValue();
			return (Integer) result;
		} catch (Exception localException) {
			localException.printStackTrace();
			L.e("————————————————————异常————————————————————");
			return -1;
		}
	}
	public static String readInput(Process proc) throws IOException {
		DataInputStream dis = new DataInputStream(proc.getInputStream());
		
		StringBuilder s = new StringBuilder();
		String str;
		while ((str = dis.readLine()) != null) {
			s.append(str).append("\n");
		}
		
		return s.toString();
	}
	
	public static String readError(Process proc) throws IOException {
		DataInputStream dis = new DataInputStream(proc.getErrorStream());
		
		StringBuilder s = new StringBuilder();
		String str;
		while ((str = dis.readLine()) != null) {
			s.append(str).append("\n");
		}
		
		return s.toString();
	}
}
