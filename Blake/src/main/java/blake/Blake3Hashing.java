package blake;
import org.apache.commons.codec.digest.Blake3;
import java.io.*;
import java.util.*;


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
      if (args[0].equals("read")) {
        readKeyHashPairs("data.bin");
        return;
      }
    Configuration conf = new Configuration();

    Path filePath = new Path("data.bin"); // Replace with your desired path
    // FileSystem fs = FileSystem.get(conf);
    // FSDataOutputStream out = fs.create(filePath);

    try (FileSystem fs = FileSystem.get(conf);
        // Path filePath = new Path("data.bin");
          FSDataOutputStream out = fs.create(filePath);) {

        long max = 1024*1024*1; 
        for (long i = 0; i < max; i++) {
            long num=i;

            byte[] key = new byte[32];
            for (int j = 0; j < key.length; j++) {
              key[j] = (byte) (num & 0xFF);
              num = num >> 8; // Shift right to access next 8 bits
            }
            // Create a Blake3 hasher
            Blake3 hasher = Blake3.initKeyedHash(key);

            // Finalize the hash and get the result
            byte[] hash = new byte[32];
            hasher.doFinalize(hash);

            // Print the hash in hexadecimal format
            // System.out.println(String.format("Key: %s Hash: %s", bytesToHex(key), bytesToHex(hash)));
            KeyHashPair pair = new KeyHashPair(key, hash);
            out.write(pair.getKey());
            out.write(pair.getHash());
            // Write the pair to the buffer
            // oos.writeObject(pair);
            // bos.write(pair.getKey());
            // bos.write(pair.getHash());
        }
      // Serialize each object
      // oos.writeObject(pair2);

      // Write serialized byte array to HDFS
      // out.write(bos.toByteArray());
      out.close();
      fs.close();
    } catch (IOException e) {
        e.printStackTrace();
      }
      
      // try (FileOutputStream fos = new FileOutputStream("data.bin");
      // BufferedOutputStream bos = new BufferedOutputStream(fos)){

      //   long max = 1024*1024*1; 
      //   for (long i = 0; i < max; i++) {
      //       long num=i;

      //       byte[] key = new byte[32];
      //       for (int j = 0; j < key.length; j++) {
      //         key[j] = (byte) (num & 0xFF);
      //         num = num >> 8; // Shift right to access next 8 bits
      //       }
      //       // Create a Blake3 hasher
      //       Blake3 hasher = Blake3.initKeyedHash(key);

      //       // Finalize the hash and get the result
      //       byte[] hash = new byte[32];
      //       hasher.doFinalize(hash);

      //       // Print the hash in hexadecimal format
      //       // System.out.println(String.format("Key: %s Hash: %s", bytesToHex(key), bytesToHex(hash)));
      //       KeyHashPair pair = new KeyHashPair(key, hash);
      //       // Write the pair to the buffer
      //       bos.write(pair.getKey());
      //       bos.write(pair.getHash());
      //   }

      //   // Flush the buffer to write remaining data to the file
      //   bos.flush();
      //   // Close the streams
      //   bos.close();
      //   fos.close();
      // }  catch (IOException e) {
      //   e.printStackTrace();
      // }
  }

  private static String bytesToHex(byte[] bytes) {
      StringBuilder sb = new StringBuilder();
      for (byte b : bytes) {
          sb.append(String.format("%02x", b));
      }
      return sb.toString();
  }

  public static void readKeyHashPairs(String filename) {
    try (FileInputStream fis = new FileInputStream(filename);
        BufferedInputStream bis = new BufferedInputStream(fis)) {
      int pairSize = 64;
      while (bis.available() > 0) {
        byte[] data = new byte[pairSize];
        int bytesRead = bis.read(data);
        if (bytesRead != pairSize) {
          break;
        }
        // pairs.add(new KeyHashPair(Arrays.copyOf(data, pairSize / 2), Arrays.copyOfRange(data, pairSize / 2, pairSize)));
        System.out.println(String.format("Key: %s Hash: %s", bytesToHex(Arrays.copyOf(data, pairSize / 2)), bytesToHex(Arrays.copyOfRange(data, pairSize / 2, pairSize))));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return;
  } 
}
