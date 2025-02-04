package blake;

import java.io.*;
import java.util.*;
import org.apache.commons.codec.digest.Blake3;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

class KeyHashPair {
  private byte[] key;
  private byte[] hash;

  public KeyHashPair(byte[] key, byte[] hash) {
    this.key = key;
    this.hash = hash;
  }

  public byte[] getKey() {
    return key;
  }

  public void setKey(byte[] key) {
    this.key = key;
  }

  public byte[] getHash() {
    return hash;
  }

  public void setHash(byte[] hash) {
    this.hash = hash;
  }
}

public class Blake3Hashing {
  public static void main(String[] args) {
    switch (args[1]) {
      case "read":
        readFromBinary("data.bin");
        break;
      case "write":
        writeToHdfs(args[2], args[3], args[4], args[5]);
        break;
      case "write2":
        writeToLocal("data.bin");
        break;
      default:
        break;
    }
  }

  private static String bytesToHex(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (byte b : bytes) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }

  public static void writeToLocal(String filename) {
    Configuration conf = new Configuration();
    Path filePath = new Path("data.bin");

    try (BufferedWriter writer = new BufferedWriter(new FileWriter("data.txt")); ) {

      long max = 1024 * 1024 * 1;
      for (long i = 0; i < max; i++) {
        long num = i;

        byte[] key = new byte[6];
        for (int j = 0; j < key.length; j++) {
          key[j] = (byte) (num & 0xFF);
          num = num >> 8; // Shift right to access next 8 bits
        }
        // Create a Blake3 hasher
        Blake3 hasher = Blake3.initHash();
        hasher.update(key);
        byte[] hash = new byte[10];
        hasher.doFinalize(hash);

        // Print the hash in hexadecimal format
        // System.out.println(String.format("Key: %s Hash: %s", bytesToHex(key), bytesToHex(hash)));
        KeyHashPair pair = new KeyHashPair(key, hash);
        String str = bytesToHex(pair.getKey()) + " " + bytesToHex(pair.getHash()) + "\n";
        writer.append(str);
      }
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void writeToHdfs(String dirname, String fileSize, String startIdx, String endIdx) {
    System.out.println(String.format("[OPERATION]: GENERATE ON HDFS"));
    System.out.println(String.format("[DIRECTORY]: %s", dirname));
    System.out.println(String.format("[FILESIZE]: %s", fileSize));
    System.out.println(String.format("[STARTIDX]: %s", startIdx));
    System.out.println(String.format("[ENDIDX]: %s", endIdx));
    Configuration conft = new Configuration();

    // Set the configuration for the HDFS instance
    // conft.set(
    //     "fs.defaultFS", "hdfs://localhost:9000"); // Change the URI according to your Hadoop setup

    try {
      // Get the HDFS instance
      FileSystem fs = FileSystem.get(conft);
      long start = 1024 * 1024 * Integer.valueOf(startIdx);

      long end = 1024 * 1024 * Integer.valueOf(endIdx);
      // if (fileSize.equals("small")) {
      //   // 16GB
      //   iter *= 1;
      // } else if (fileSize.equals("medium")) {
      //   // 32GB
      //   iter *= 2;
      // } else if (fileSize.equals("medium")) {
      //   // 64GB
      //   iter *= 4;
      // }
      System.out.println(String.format("[NO_RECORDS]: %dG", (end-start)/1024/1024));
      int flushSize=1024 * 1024;
      System.out.println(String.format("[FLUSH SIZE]: %d records", flushSize));

      // Create a new file in HDFS
      Path filePath = new Path(String.format("%s/data%d.txt", dirname, Integer.valueOf(startIdx)/128));
      OutputStream os = fs.create(filePath);
      StringBuilder sb = new StringBuilder();

      for (long i = start; i < end; i++) {
        long num = i;

        byte[] key = new byte[6];
        for (int j = 0; j < key.length; j++) {
          key[j] = (byte) (num & 0xFF);
          num = num >> 8; // Shift right to access next 8 bits
        }
        // Create a Blake3 hasher
        Blake3 hasher = Blake3.initHash();
        hasher.update(key);
        byte[] hash = new byte[10];
        hasher.doFinalize(hash);

        // Print the hash in hexadecimal format
        // KeyHashPair pair = new KeyHashPair(key, hash);

        for (byte b : key) {
          sb.append(String.format("%02x", b));
        }
        // sb.append(bytesToHex(key));
        sb.append(" ");
        for (byte b : hash) {
          sb.append(String.format("%02x", b));
        }
        // sb.append(bytesToHex(hash));
        sb.append("\n");
        // String str = bytesToHex(pair.getKey()) + " " + bytesToHex(pair.getHash()) + "\n";
        if (i % flushSize == 0 && i>0) {
          System.out.println(
              String.format("\t Flush cycle: %d, %d records", i / flushSize, i));
          os.write(sb.toString().getBytes("UTF-8"));
          sb = new StringBuilder();
          os.flush();
          // break;
          // os.flush();
          // os.close();
          // fileidx += 1;
          // filePath = new Path(String.format("%s/data%d.txt", dirname, fileidx));
          // os = fs.create(filePath);
        }
      }
      os.flush();
      os.close();

      System.out.println("File created successfully in HDFS!");

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void readFromBinary(String filename) {
    try (FileInputStream fis = new FileInputStream(filename);
        BufferedInputStream bis = new BufferedInputStream(fis)) {
      int pairSize = 16;
      while (bis.available() > 0) {
        byte[] data = new byte[pairSize];
        int bytesRead = bis.read(data);
        if (bytesRead != pairSize) {
          break;
        }
        System.out.println(
            String.format(
                "Key: %s Hash: %s",
                bytesToHex(Arrays.copyOf(data, 6)), bytesToHex(Arrays.copyOfRange(data, 6, 16))));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return;
  }
}
