import java.util.Hashtable;
import java.io.*;
import java.lang.Thread;

class Disk
{
    static final int NUM_SECTORS = 2048;
    static final int DISK_DELAY = 80;
    StringBuffer sectors[] = new StringBuffer[NUM_SECTORS];

    Disk()
    {
    }

    void write(int sector, StringBuffer data)  // call sleep
    {
        sectors[sector] = data;
        try {
            Thread.sleep(DISK_DELAY);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
            return;
        }
    }

    void read(int sector, StringBuffer data)   // call sleep
    {
        data.append(sectors[sector]);
        try {
            Thread.sleep(DISK_DELAY);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
            return;
        }
    }
}

class Printer
{
    static final int PRINT_DELAY = 275;
    FileWriter myWriter;

    Printer(int id)
    {
        String printerFile = "PRINTER" + id;
        try {
            myWriter = new FileWriter(printerFile);
            // myWriter.write("HELLO");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void print(StringBuffer data)  // call sleep
    {
        try {
            myWriter.write(data.toString() + "\n");
            myWriter.flush();
            Thread.sleep(PRINT_DELAY);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
            return;
        }
    }
}

class PrintJobThread extends Thread
{
    StringBuffer line = new StringBuffer(); // only allowed one line to reuse for read from disk and print to printer
    private String fileToPrint;

    PrintJobThread(String fileToPrint)
    {
        this.fileToPrint = fileToPrint;
    }

    @Override
    public void run()
    {
        MainClass os = MainClass.getInstance();
        FileInfo fileInfo = os.diskManager.directory.lookup(new StringBuffer(fileToPrint));

        int diskNumber = fileInfo.diskNumber;
        int startingSector = fileInfo.startingSector;
        int fileLength = fileInfo.fileLength;
        int printerNumber = os.printerManager.request();

        for (int i = 0; i < fileLength; i++) {
            int sector = startingSector + i;
            StringBuffer data = new StringBuffer();
            os.disks[diskNumber].read(sector, data);
            os.printers[printerNumber].print(data);
        }

        os.printerManager.release(printerNumber);
    }
}

class FileInfo
{
    int diskNumber;
    int startingSector;
    int fileLength;

    FileInfo(int diskNumber, int startingSector, int fileLength) {
        this.diskNumber = diskNumber;
        this.startingSector = startingSector;
        this.fileLength = fileLength;
    }
}

class DirectoryManager
{
    private Hashtable<String, FileInfo> T = new Hashtable<String, FileInfo>();

    DirectoryManager()
    {
    }

    void enter(StringBuffer fileName, FileInfo file)
    {
        T.put(fileName.toString(), file);
    }

    FileInfo lookup(StringBuffer fileName)
    {
        return T.get(fileName.toString());
    }
}

class ResourceManager
{
    boolean isFree[];
    ResourceManager(int numberOfItems) {
        isFree = new boolean[numberOfItems];
        for (int i=0; i<isFree.length; ++i)
            isFree[i] = true;
    }
    synchronized int request() {
        while (true) {
            for (int i = 0; i < isFree.length; ++i) {
                if ( isFree[i] ) {
                    isFree[i] = false;
                    return i;
                }
                try {
                    this.wait(); // block until someone releases Resource
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }
        }
    }

    synchronized void release( int index ) {
        isFree[index] = true;
            this.notify(); // let a blocked thread run
    }
}

class DiskManager extends ResourceManager
{
    DirectoryManager directory = new DirectoryManager();
    int freeSectors[];

    DiskManager(int numberOfItems) {
        super(numberOfItems);
        freeSectors = new int[numberOfItems];
    }

    void updateFreeSectors(StringBuffer fileName, FileInfo file) {
        directory.enter(fileName, file);
        int diskNumber = file.diskNumber;
        int freeSector = file.startingSector + file.fileLength;
        freeSectors[diskNumber] = freeSector;
    }
}

class PrinterManager extends ResourceManager
{
    PrinterManager(int numberOfItems) {
        super(numberOfItems);
    }
}

class UserThread extends Thread
{
    private int id;
    private String fileName;
    private int diskNumber;
    private int startingSector;
    private int fileLength;

    UserThread(int id) // my commands come from an input file with name USERi where i is my user id
    {
        this.id = id;
    }

    @Override
    public void run()
    {
        try {
            String userFile = "USER" + id;
            FileInputStream inputStream = new FileInputStream(userFile);
            BufferedReader myReader = new BufferedReader(new InputStreamReader(inputStream));

            for (String line; (line = myReader.readLine()) != null;) {
                processUserCommand(line);
            }

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void processUserCommand(String line) {
        String[] args = line.split(" ");
        MainClass os = MainClass.getInstance();
        if (args[0].equals(".save")) {
            fileName = args[1];
            diskNumber = os.diskManager.request();
            startingSector = os.diskManager.freeSectors[diskNumber];
            fileLength = 0;
        } else if (args[0].equals(".end")) {
            os.diskManager.release(diskNumber);
            FileInfo fileInfo = new FileInfo(diskNumber, startingSector, fileLength);
            os.diskManager.updateFreeSectors(new StringBuffer(fileName), fileInfo);
        } else if (args[0].equals(".print")) {
            String _fileName = args[1];
            PrintJobThread printJobThread = new PrintJobThread(_fileName);
            printJobThread.start();
        } else {
            int sector = startingSector + fileLength;
            os.disks[diskNumber].write(sector, new StringBuffer(line));
            fileLength++;
        }
    }
}


public class MainClass
{
    private static MainClass instance = null;
    int NUM_USERS = 1, NUM_DISKS = 1, NUM_PRINTERS = 1;
    // String userFileNames[];
    UserThread users[];
    Disk disks[];
    Printer printers[];
    DiskManager diskManager;
    PrinterManager printerManager;

    private MainClass(String args[]) {
        // configure first with args in HW9
        users = new UserThread[NUM_USERS];
        disks = new Disk[NUM_DISKS];
        printers = new Printer[NUM_PRINTERS];

        users[0] = new UserThread(0); // 1 user used for HW8
        disks[0] = new Disk(); // 1 disk used for HW8
        printers[0] = new Printer(0); // 1 printer used for HW8

        diskManager = new DiskManager(NUM_DISKS);
        printerManager = new PrinterManager(NUM_PRINTERS);
    }

    private void startUserThreads() {
        for (int i = 0; i < NUM_USERS; i++)
            users[i].start();
    }

    private void joinUserThreads() {
        for (int i = 0; i < NUM_USERS; i++) {
            try {
                users[i].join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static MainClass createInstance(String args[]) {
        if (instance == null) {
            instance = new MainClass(args);
        }
        return instance;
    }

    static MainClass getInstance() {
        return instance;
    }

    public static void main(String args[])
    {
        for (int i=0; i<args.length; ++i)
            System.out.println("Args[" + i + "] = " + args[i]);
            
        System.out.println("*** 141 OS Simulation ***");
        MainClass os = MainClass.createInstance(args);
        os.startUserThreads();
        os.joinUserThreads();
    }
}
