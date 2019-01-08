import java.util.ArrayList;
import java.util.Random;

public class MailBox {

    //MAILBOX
    ArrayList<TriTuple> mail = new ArrayList<>();

    public MailBox() {
        TriTuple media = new TriTuple("Media Player");
        TriTuple web = new TriTuple("Web Browser");
        TriTuple virus = new TriTuple("Virus Scanner");
        TriTuple word = new TriTuple("Word Processor");
        TriTuple photo = new TriTuple("Photo Editor");

        mail.add(media);
        mail.add(web);
        mail.add(virus);
        mail.add(word);
        mail.add(photo);
    }

    public ArrayList<TriTuple> getMail() {
        return mail;
    }

    public String toString(){
        String result = "";

        for(TriTuple mailbox:mail){
            result += "Mailbox Type - " + mailbox.type + " : Data " + mailbox.data + " : Lock " + mailbox.lock + "\n\n";
        }

        return result;
    }

    public class TriTuple {

        Random rand = new Random();
        String type;
        int lock;
        String data;

        public TriTuple(String type){
            this.type = type;
            this.data = "Entry 0";
            this.lock = rand.nextInt();
        }

        public String getMessage(int key){
            if(key == this.lock) {
                return this.data;
            } else {
                return null;
            }
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}

