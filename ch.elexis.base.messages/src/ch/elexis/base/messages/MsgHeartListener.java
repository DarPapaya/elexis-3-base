/*******************************************************************************
 * Copyright (c) 2007-2008, G. Weirich and Elexis
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    G. Weirich - initial implementation
 *    
 *******************************************************************************/

package ch.elexis.base.messages;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.elexis.core.constants.Preferences;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.core.data.events.Heartbeat.HeartListener;
import ch.elexis.core.ui.Hub;
import ch.elexis.core.ui.UiDesk;
import ch.elexis.data.Query;
import ch.elexis.messages.Message;

public class MsgHeartListener implements HeartListener {
	static Logger log = LoggerFactory.getLogger(MsgHeartListener.class);
	boolean bSkip;
	
	public void heartbeat(){
		if (!bSkip) {
			if (CoreHub.actUser != null) {
				Query<Message> qbe = new Query<Message>(Message.class);
				qbe.add("to", "=", CoreHub.actUser.getId()); //$NON-NLS-1$ //$NON-NLS-2$
				final List<Message> res = qbe.execute();
				if (res.size() > 0) {
					UiDesk.getDisplay().asyncExec(new Runnable() {
						public void run(){
							bSkip = true;
							if (CoreHub.userCfg.get(Preferences.USR_MESSAGES_SOUND_ON, true)) {
								playSound();
							}
							new MsgDetailDialog(Hub.getActiveShell(), res.get(0)).open();
							bSkip = false;
						}
					});
					
				}
			}
		}
	}
	
	/**
	 * plays a sound. the sound file can be defined via the message preferences
	 */
	private void playSound(){
		try {
			AudioInputStream audioInStream;
			String soundFilePath =
				CoreHub.userCfg.get(Preferences.USR_MESSAGES_SOUND_PATH,
					MessagePreferences.DEF_SOUND_PATH);
			
			// create an audioinputstream from sound url
			if (MessagePreferences.DEF_SOUND_PATH.equals(soundFilePath)) {
				URL sound = getClass().getResource(soundFilePath);
				
				audioInStream = AudioSystem.getAudioInputStream(sound);
			} else {
				// create AudioInputStream from user defined file
				File soundFile = new File(soundFilePath);
				if (!soundFile.exists()) {
					log.warn("Sound file [" + soundFilePath + "] not found");
					return;
				}
				audioInStream = AudioSystem.getAudioInputStream(soundFile);
			}
			
			// load the sound into memory (a Clip)
			Clip clip = AudioSystem.getClip();
			clip.open(audioInStream);
			clip.start();
			
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			log.error("Could not play message sound", e);
			return;
		}
	}
}
