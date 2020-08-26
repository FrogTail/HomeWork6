/*
    Создать 2 текстовых файла, примерно по 50-100 символов в каждом(особого значения не имеет);

    Написать программу, «склеивающую» эти файлы,
    то есть вначале идет текст из первого файла, потом текст из второго.

    * Написать программу, которая проверяет присутствует ли указанное пользователем слово в файле
    (работаем только с латиницей).

    ** Написать метод, проверяющий, есть ли указанное слово в папке
*/

import java.io.*;
import java.util.Scanner;

public class HomeWork6 {

    // Массив можно расширить, вбил только самое основное
    public static final char[] separators = {' ', '\n', '.', ',', '!', '?', ':'};

    public static void main (String[] args)
    {
        try
        {
            System.out.printf ("==== Склеиваем песню из куплетов и припевов. ====");
            glueSong();
            System.out.printf ("\nРезультат в файле Land_Of_Confusion.txt");


            String word = "street";
            String path = "verse1.txt";
            System.out.printf ("\n\n==== Ищем слово \"%s\" в файле \"%s\"", word, path);
            System.out.printf("\n"+findWordInFile(word, path, true));


            word = "CoNfUsIoN";
            System.out.printf("\n\n==== Ищем слово \"%s\" во всех TXT в текущей папке", word);
            // Костыльный метод получения адреса текущей папки
            File dir = new File("");
            String currentDirPath = dir.getAbsolutePath();
            System.out.printf("\n"+findWordInDir(word,currentDirPath, false));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    // Ищем в _СЛОВО_ во всех txt в указанной папке
    public static boolean findWordInDir (String word, String path, Boolean caseSensitive) throws IOException
    {
        File dir = new File(path);

        if (!dir.isDirectory())
        {
            IOException e = new IOException("It isn't a folder.");
            throw e;
        }

        String[] dirList = dir.list();

        for (int i=0; i<dirList.length; i++)
        {
            if(dirList[i].endsWith(".txt"))
            {
                if (findWordInFile(word,dirList[i],caseSensitive))
                    return true;
            }
        }

        return false;
    }


    // Проверяем наличие _СЛОВА_ в файле
    public static boolean findWordInFile (String word, String path, Boolean caseSensitive) throws IOException
    {
        if (!isOneWord(word))
        {
            IOException e = new IOException("Sent text contents separators.");
            throw e;
        }

        if (!caseSensitive)
            word = word.toLowerCase();

        // Решил вот так сделать, чтобы потестить стрингбилдер
        StringBuilder memorized = new StringBuilder();

        FileInputStream fis = new FileInputStream(path);

        int b;
        while ((b=fis.read())!=-1)
        {
            if (!caseSensitive)
                memorized.append(Character.toLowerCase((char)b));
            else
                memorized.append((char)b);
        }
        fis.close();

        StringBuilder currentWord = new StringBuilder();

        for (int i=0; i<memorized.length(); i++)
        {
            boolean append = true;

            if (isSeparator(memorized.charAt(i)))
            {
                if (currentWord.toString().equals(word))
                    return true;
                else
                {
                    currentWord.delete(0,currentWord.length());
                    append = false;
                }
            }

            if (append)
                currentWord.append(memorized.charAt(i));
        }

        // Проверяем последнее слово, тк оно не проверялось в цикле
        if (currentWord.toString().equals(word))
            return true;
        else
            return false;
    }


    // Является ли присланный фрагмент текста одним словом
    public static boolean isOneWord (String text)
    {
        char[] bySymbols = text.toCharArray();

        for (int i=0; i<bySymbols.length; i++)
        {
            if (isSeparator(bySymbols[i]))
                return false;
        }

        return true;
    }


    // Является ли символ разделителем
    public static boolean isSeparator (char c)
    {
        for (int i=0; i<separators.length; i++)
        {
            if (c==separators[i])
                return true;
        }
        return false;
    }


    // Проверяем наличие _ФРАГМЕНТА ТЕКСТА_ в файле
    public static boolean findTextInFile (String text, String path, boolean caseSensitive) throws IOException
    {
        byte[] byCodes = text.getBytes();

        FileInputStream fis = new FileInputStream(path);

        int b;
        int matches = 0;
        int letterPosition = 0;
        while ((b=fis.read()) != -1)
        {
            // чтобы игнорить регистр
            char firstChar = (char)b;
            char secondChar = (char)byCodes[matches];

            if (!caseSensitive)
            {
                firstChar = Character.toLowerCase(firstChar);
                secondChar = Character.toLowerCase(secondChar);
            }

            if (firstChar == secondChar)
                matches++;
            else
                matches = 0;


            if (matches == byCodes.length-1)
            {
                fis.close();
                return true;
            }
        }
        fis.close();
        return false;
    }


    // Склеиваем песню (просто ради теста)
    public static void glueSong () throws IOException
    {
        // Очищаем файл
        FileOutputStream fos = new FileOutputStream("Land_Of_Confusion.txt");
        fos.flush();
        fos.close();

        // Немного индусятины :)
        glueFiles("Land_Of_Confusion.txt","verse1.txt",0);
        glueFiles("Land_Of_Confusion.txt","verse2.txt",2);
        glueFiles("Land_Of_Confusion.txt","verse3.txt",2);

        glueFiles("Land_Of_Confusion.txt","chorus1-2.txt",2);

        glueFiles("Land_Of_Confusion.txt","verse4.txt",2);
        glueFiles("Land_Of_Confusion.txt","verse5.txt",2);

        glueFiles("Land_Of_Confusion.txt","chorus1-2.txt",2);

        glueFiles("Land_Of_Confusion.txt","verse6.txt",2);
        glueFiles("Land_Of_Confusion.txt","verse7.txt",2);
        glueFiles("Land_Of_Confusion.txt","verse8.txt",2);

        glueFiles("Land_Of_Confusion.txt","chorus3.txt",2);
        glueFiles("Land_Of_Confusion.txt","chorus4.txt",2);
    }

    // Склеиваем два файла (добавляем второй в конец первого)
    public static void glueFiles (String path1, String path2, int lineBreaks) throws IOException
    {
        FileInputStream fis = new FileInputStream(path2);
        FileOutputStream fos = new FileOutputStream(path1,true);

        for (int i=0; i<lineBreaks; i++)
        {
            fos.write('\n');
        }

        int b;

        while ((b=fis.read()) != -1)
        {
            fos.write(b);
        }

        fos.flush();
        fos.close();

        fis.close();
    }
}
