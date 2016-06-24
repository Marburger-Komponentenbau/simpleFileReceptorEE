package com.conetex.simpleFileReceptorEE;

import java.io.IOException;

public class ProcessWithTimeout extends Thread {
	
	private ProcessBuilder m_process;
	private int m_exitCode = Integer.MIN_VALUE;

	public ProcessWithTimeout(ProcessBuilder p_process) {
		m_process = p_process;
	}

	public int waitForProcess(int p_timeoutMilliseconds) {
		this.start();
		try {
			this.join(p_timeoutMilliseconds);
		} catch (InterruptedException e) {
			this.interrupt();
		}
		return m_exitCode;
	}

	@Override
	public void run() {
		Process p;
		try {
			p = m_process.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		try {
			m_exitCode = p.waitFor();
		} catch (InterruptedException ignore) {
			// Do nothing
		} catch (Exception ex) {
			// Unexpected exception
		}
	}

}