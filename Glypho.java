/*

Interpreter for the Glypho language

Rune Berge 2005
http://rune.krokodille.com/lang

Glypho specification:
http://www4.ncsu.edu/~bcthomp2/glypho.txt

This software is Public Domain.

*/


import java.io.*;

public class Glypho {

  public static void displayError(String msg) {
    System.err.println(msg);
    System.exit(1);
  }

  public static void main(String[] args) {
    StringBuffer source = new StringBuffer();
    InputStream in = System.in;
    OutputStream out = System.out;
    OutputStream err = System.err;

    if (args.length==0) displayUsage();

    for (int i = 0; i < args.length; i++) {
      if (args[i].charAt(0) == '-') {
        switch (args[i].charAt(1)) {
          case 'i': if ((i++) < (args.length - 1))
                      try {
                        in = new FileInputStream(args[i]);
                      }catch (FileNotFoundException e) { displayError("File not found: " + args[i]); }
                    else displayError("Missing input filename");
                    break;
          case 'o': if ((i++) < (args.length - 1))
                      try {
                        out = new FileOutputStream(args[i]);
                      }catch (FileNotFoundException e) { displayError("Unable to write to " + args[i]); }
                    else displayError("Missing output filename");
                    break;
          case 'e': if ((i++) < (args.length - 1))
                      try {
                        err = new FileOutputStream(args[i]);
                      }catch (FileNotFoundException e) { displayError("Unable to write to " + args[i]); }
                    else displayError("Missing error filename");
                    break;
          case 'h': displayUsage();
                    break;
          default:  displayError("Invalid argument: " + args[i]);

        }

      }
    }

    if (args.length <= 0) displayError("Error: No source file specified!");

    if (args.length >= 1) {
      String sourceFile = args[args.length-1];

      try {
        FileInputStream src = new FileInputStream(sourceFile);
        int b=-1;

        b = src.read();
        while (b>=0)  {
          source.append((char)b);
          b = src.read();
        }

      } catch (FileNotFoundException e) { displayError("File not found: " + args[args.length-1]); }
      catch (IOException e) { displayError(e.toString());}


      boolean shorthand = false;
      if (sourceFile.substring(sourceFile.length() - 4).equals(".gsh")) shorthand = true;

      new Glypho(source.toString(), shorthand, in, out, err);

    }

  }//main()

  public static void displayUsage() {
    System.out.println("Glypho Interpreter version " + GlyphoInterpreter.VERSION);
    System.out.println("Rune Berge - 2005 - http://rune.krokodille.com/lang");
    System.out.println("Glypho specification: http://www4.ncsu.edu/~bcthomp2/glypho.txt");
    System.out.println("");
    System.out.println("This is software is Public Domain");
    System.out.println("");
    System.out.println("Usage: Glypho [OPTIONS] [filename]");
    System.out.println("");
    System.out.println("If the filename ends with .gsh it will be interpreted as Glypho shorthand.");
    System.out.println("Otherwise it will be interpreted as regular Glypho.");
    System.out.println("");
    System.out.println("Options:");
    System.out.println("  -i <filename>   Read input from this file (default is standard input)");
    System.out.println("  -o <filename>   Write output to this file (default is standard output)");
    System.out.println("  -e <filename>   Write error messages to this file (default is standard output)");
    System.out.println("  -h              Display this information");
    System.out.println("");
    System.out.println("");
    System.out.println("Please report any bugs to rune@krokodille.com");


    System.exit(0);
  }

  public Glypho(String source, boolean shorthand, InputStream in, OutputStream out, OutputStream err) {

    GlyphoInterpreter GI = new GlyphoInterpreter(in, out, err);
    GI.interpret(source, shorthand);
  }

}