// Copyright 2011-2012 Paulo Augusto Peccin. See licence.txt distributed with this file.

package org.javatari.pc.screen;

import org.javatari.atari.cartridge.CartridgeSocket;
import org.javatari.atari.controls.ConsoleControlsSocket;
import org.javatari.general.av.video.VideoSignal;

import java.awt.*;
import java.util.List;

public interface Screen {

  public void connect(VideoSignal videoSignal, ConsoleControlsSocket controlsSocket, CartridgeSocket cartridgeSocket);

  public Monitor monitor();

  public List<Component> keyControlsInputComponents();

  public void powerOn();

  public void powerOff();

  public void close();

  public void destroy();

}