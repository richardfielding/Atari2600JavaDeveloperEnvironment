// Copyright 2011-2012 Paulo Augusto Peccin. See licence.txt distributed with this file.

package org.javatari.main;

import org.javatari.parameters.Parameters;
import org.javatari.pc.room.Room;
import org.javatari.utils.Environment;
import org.javatari.utils.SwingHelper;
import org.javatari.utils.Terminator;

import javax.swing.*;
import java.io.IOException;

public final class MultiplayerClient {

  public static void main(String[] args) throws IOException, InterruptedException {

    // Initialize application environment
    Environment.init();

    // Load Parameters from properties file and process arguments
    Parameters.init(args);

    // Build a ClientRoom for P2 Client play and turn everything on
    final Room clientRoom = Room.buildClientRoom();
    clientRoom.powerOn();

    // Start connection to P1 Server
    askUserForConnection(clientRoom);

  }

  public static void askUserForConnection(final Room room) {
    SwingHelper.edtInvokeLater(new Runnable() {
      @Override
      public void run() {
        String server = Parameters.mainArg;
        while (true) {
          // Try connecting to server
          if (server != null && !server.isEmpty()) {
            try {
              room.clientCurrentConsole().remoteReceiver().connect(server);
              return;
            } catch (Exception ex) {
              JOptionPane.showMessageDialog(null, "Unnable to connect to: " + server + "\n" + ex, "Atari Player 2 Client", JOptionPane.ERROR_MESSAGE);
            }
          }
          // If unsuccessful, ask for another address
          do {
            server = (String) JOptionPane.showInputDialog(
              null, "Atari Player 1 Server address[:port]:", "Atari Player 2 Client",
              JOptionPane.PLAIN_MESSAGE, null, null,
              (server != null && !server.isEmpty()) ? server : "localhost"
            );
            if (server == null) Terminator.terminate();  // User chose to cancel
            server = server.trim();
          } while (server.isEmpty());
        }
      }
    });
  }

}
