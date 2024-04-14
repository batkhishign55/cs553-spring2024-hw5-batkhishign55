package blake;
import org.apache.commons.codec.digest.Blake3;
import java.io.*;
import java.util.*;

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
      
      try (FileOutputStream fos = new FileOutputStream("data.bin");
      BufferedOutputStream bos = new BufferedOutputStream(fos)){
        
        long max = 1024*1024*16; 
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
            // Write the pair to the buffer
            bos.write(pair.getKey());
            bos.write(pair.getHash());
        }

        // Flush the buffer to write remaining data to the file
        bos.flush();
        // Close the streams
        bos.close();
        fos.close();
      }  catch (IOException e) {
        // Handle file writing exception (e.g., disk full, permission issues)
        e.printStackTrace();
        // throw e; // Re-throw the exception for caller to handle
      }
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
