package jni;

import javax.swing.*;

class JniException extends Exception {

	JniException(int errorCode) {
		super("JNI Error occurs (Error code: " + errorCode + " )");
	}

	@Override
	public void printStackTrace() {
		super.printStackTrace();
		JOptionPane.showMessageDialog(null,
				getMessage(), "JNI Error", JOptionPane.ERROR_MESSAGE);
	}
}
