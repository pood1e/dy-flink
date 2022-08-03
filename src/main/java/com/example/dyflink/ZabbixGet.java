package com.example.dyflink;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * @author pood1e
 */
public class ZabbixGet {
	public static void main(String[] args) throws IOException {
		List<String> keys = List.of("vm.memory.size[pavailable]",
				"disk.status[rb]",
				"disk.status[wb]",
				"system.cpu.util[,user,]",
				"system.cpu.util[,nice,]",
				"system.cpu.util[,system,]",
				"system.cpu.util[,iowait,]",
				"system.cpu.util[,interrupt,]",
				"system.cpu.util[,softirq,]",
				"system.cpu.util[,steal,]",
				"system.cpu.util[,idle,]"
		);
		for (String key : keys) {
			Socket socket = new Socket("10.1.1.5", 10050);
			OutputStream output = socket.getOutputStream();
			BufferedInputStream input = new BufferedInputStream(socket.getInputStream());
			output.write(pack(key));
			byte[] result = input.readAllBytes();
			byte[] parsed = new byte[result.length - 13];
			System.arraycopy(result, 13, parsed, 0, result.length - 13);
			System.out.println(new String(parsed));
			socket.close();
		}
	}

	private static byte[] pack(String key) {
		byte[] data = key.getBytes(StandardCharsets.UTF_8);
		byte[] header = new byte[]{'Z', 'B', 'X', 'D', '\1', (byte) (data.length & 0xFF), (byte) ((data.length >> 8) & 0xFF), (byte) ((data.length >> 16) & 0xFF), (byte) ((data.length >> 24) & 0xFF), '\0', '\0', '\0', '\0'};
		byte[] packet = new byte[header.length + data.length];
		System.arraycopy(header, 0, packet, 0, header.length);
		System.arraycopy(data, 0, packet, header.length, data.length);
		return packet;
	}
}
