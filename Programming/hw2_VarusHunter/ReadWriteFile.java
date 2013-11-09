package hw2_VarusHunter;

public class ReadWriteFile
{
  String file;
  int max_bytes = 300; // max one time read-write.

  public String read(){
    // read max_bytes from file.
  }

  public boolean write(String content){
    // write str to file.
    // Args: 
    //   content: the content to export
    // Returns:
    //   True if write to file, False if exception.
    try {
      FileWriter fw = new FileWriter(this.file);
      fw.write(content);
      fw.flush();
      fw.close();
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public void close(){
    // close file.
  }

  public void __init__(String file){
    this.file = file;
  }

  ReadWriteFile(String file){
    this.__init__(file);
  }
}

