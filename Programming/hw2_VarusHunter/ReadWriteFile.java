package hw2_VarusHunter;

public class ReadWriteFile
{
  String file;
  int max_bytes = 300; // max one time read-write.

  public String read(){
    // read max_bytes from file.
  }

  public void write(String str){
    // write str to file.
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

