package com.yz.snake;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

public class SnakeScore {
	
	private static short[] highScore = new short[SnakeBody.MAX_LEVELS];

	private static String[] highScoreName = new String[SnakeBody.MAX_LEVELS];

	private static RecordStore myStore;

	private static boolean highScoresHaveBeenInit;

	private SnakeScore() {
	}

	private static void initializeScores() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		byte[] b;

		try {
			try {
				dos.writeShort(0);
				dos.writeUTF("");
				b = baos.toByteArray();
				dos.close();
			} catch (IOException ioe) {
				throw new RecordStoreException();
			}

			for (int i = 0; i < SnakeBody.MAX_LEVELS; i++) {
				myStore.addRecord(b, 0, b.length);
			}
		} catch (RecordStoreException rse) {
			closeHighScores();
		}
	}

	static void openHighScores() {
		try {
			myStore = RecordStore.openRecordStore("HighScores", true);

			if (highScoresHaveBeenInit) {
				return;
			}

			if (myStore.getNumRecords() == 0) {
				initializeScores();
			} else {
				ByteArrayInputStream bais;
				DataInputStream dis;
				byte[] data;

				for (int i = 0; i < SnakeBody.MAX_LEVELS; i++) {
					data = myStore.getRecord(i + 1);

					if (data != null) {
						try {
							bais = new ByteArrayInputStream(data);
							dis = new DataInputStream(bais);
							highScore[i] = dis.readShort();
							highScoreName[i] = dis.readUTF();
							dis.close();
						} catch (IOException ioe) {
						}
					}
				}
			}

			highScoresHaveBeenInit = true;
		} catch (RecordStoreException rse) {
		}
	}

	static void closeHighScores() {
		if (myStore != null) {
			try {
				myStore.closeRecordStore();
			} catch (RecordStoreException frse) {
			}

			myStore = null;
		}
	}

	static void setHighScore(int level, int newScore, String name) {
		ByteArrayOutputStream baos;
		DataOutputStream das;
		byte[] data;

		if (newScore <= highScore[level]) {
			return;
		}

		try {
			try {
				baos = new ByteArrayOutputStream();
				das = new DataOutputStream(baos);

				das.writeShort((short) newScore);
				das.writeUTF(name);
				data = baos.toByteArray();
				das.close();
			} catch (IOException ioe) {
				throw new RecordStoreException();
			}

			if (myStore == null) {
				openHighScores();
				myStore.setRecord(level + 1, data, 0, data.length);
				closeHighScores();
			} else {
				myStore.setRecord(level + 1, data, 0, data.length);
			}
		} catch (RecordStoreException rse) {
		}

		highScore[level] = (short) newScore;
		highScoreName[level] = name;
	}

	static short getHighScore(int level) {
		if (!highScoresHaveBeenInit) {
			openHighScores();
			closeHighScores();
		}

		return highScore[level];
	}

	static String getHighScoreName(int level) {
		if (!highScoresHaveBeenInit) {
			openHighScores();
			closeHighScores();
		}

		return highScoreName[level];
	}
}
