package com.setuga.process;

import java.io.*;
import java.util.Scanner;

public class Main
{

    private static final String CHARSET_NAME = "SHIFT_JIS";

    static class BuffedReaderThread extends Thread
    {
        private InputStream t_inputStream;

        BuffedReaderThread(InputStream inputStream)
        {
            t_inputStream = inputStream;
        }

        @Override
        public void run()
        {
            try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(t_inputStream, CHARSET_NAME)))
            {
                String readLine = bufferedReader.readLine();
                while (readLine != null)
                {
                    System.out.println(readLine);
                    readLine = bufferedReader.readLine();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    static class BufferedWriterThread extends Thread
    {
        private OutputStream t_outputStream;

        BufferedWriterThread(OutputStream outputStream)
        {
            t_outputStream = outputStream;
        }

        @Override
        public void run()
        {
            Scanner scanner = new Scanner(System.in);
            try(BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(t_outputStream, CHARSET_NAME)))
            {
                while (scanner.hasNext())
                {
                    String readLine = scanner.nextLine();
                    //System.out.println(readLine);
                    bufferedWriter.write(readLine);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException
    {
        Process process = new ProcessBuilder("cmd.exe", "start").start();

        BuffedReaderThread buffedInputReaderThread = new BuffedReaderThread(process.getInputStream());
        BuffedReaderThread buffedErrorReaderThread = new BuffedReaderThread(process.getErrorStream());
        BufferedWriterThread bufferedWriterThread = new BufferedWriterThread(process.getOutputStream());

        buffedInputReaderThread.start();
        buffedErrorReaderThread.start();
        bufferedWriterThread.start();
    }

}
