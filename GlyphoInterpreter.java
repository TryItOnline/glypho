// This software is Public Domain.

import java.util.Stack;
import java.io.*;

public class GlyphoInterpreter  {
  public static final char[] DEBUG_OPERATORS = "&".toCharArray();
  public static final String VERSION = "0.2";
  public static final char[] OPERATORS = "niod\\><!1+-*e[]".toCharArray();
  public static final char EOL = 10;
  public static final char COMMENT_START = '#';
  public static final char COMMENT_END = EOL;
  protected InputStream in;
  protected OutputStream out;
  protected PrintStream err;

  protected static final char[][] shorthandOps = {
    {   0, 'n'},
    {   1, 'i'},
    {  10, '>'},
    {  11, '\\'},
    {  12, '1'},
    { 100, '<'},
    { 101, 'd'},
    { 102, '['},
    { 110, '+'},
    { 111, 'o'},
    { 112, '*'},
    { 120, '-'},
    { 121, ']'},
    { 122, '!'},
    { 123, 'e'}
  };


  public GlyphoInterpreter() {
    this(System.in, System.out, System.err);
  }

  public GlyphoInterpreter(InputStream in, OutputStream out, OutputStream err) {
    this.in = in;
    this.out = out;
    this.err = new PrintStream(err);
  }


  public boolean interpret(String source, boolean shorthand) {

    Operator o = tokenize(source, shorthand);
    while (o != null) {
      o.execute();
      o = o.conditionalNext();
    }

    return true;
  }//interpret()



  public String removeComments(String source) {
    StringBuffer buf = new StringBuffer(source);
    int pos = 0, cStart=-1;
    while (pos < buf.length()) {
      if ((buf.charAt(pos)==COMMENT_START) && (cStart < 0)) cStart=pos;
      if ((buf.charAt(pos)==COMMENT_END) && (cStart >= 0)) {
        buf.delete(cStart, pos);
        pos = cStart;
        cStart=-1;
      }
      pos++;
    }
    if (cStart>0) buf.delete(cStart, buf.length());
    return buf.toString();
  }//removeComments

  public static boolean isOperator(char ch) {
    for (int i = 0; i < OPERATORS.length; i++) if (OPERATORS[i] == ch) return true;
    for (int i = 0; i < DEBUG_OPERATORS.length;  i++) if (DEBUG_OPERATORS[i]  == ch) return true;
    return false;
  }


  public Operator tokenize(String str, boolean shorthand) {
    char[] source;
    if (shorthand) {
      source = removeComments(str).toCharArray();
    }else{
      source = new char[str.length() / 4];
      char[] patterns = str.toCharArray();
      for (int i = 0; i < source.length; i++) {
        source[i] = getOperator(new int[]
          {
            (int)patterns[i*4],
            (int)patterns[i*4 + 1],
            (int)patterns[i*4 + 2],
            (int)patterns[i*4 + 3]
          }
        );
      }
    }

    Operator.in = in;
    Operator.out = out;
    Operator.err = err;
    Operator operator = null, firstOperator = null;

    for (int i = 0; i < source.length; i++) {
      if (isOperator(source[i])) {
        if (firstOperator == null) {
          firstOperator = new Operator(source[i]);
          operator = firstOperator;
        }else{
          operator.setNext(new Operator(source[i]));
          operator = operator.next();
        }
      }
    }
    return firstOperator;
  }//tokenize()

  protected static String parsePattern(int[] pattern)  {
    String res = "0";
    int n = 0;
    for (int i = 1; i < pattern.length; i++) {
      char ch = 0;
      for (int j = 0; j < i; j++) {
        if (pattern[i]==pattern[j]) {
          ch = res.charAt(j);
          break;
        }
      }
      if (ch == 0) ch = (char)('0' + ++n);
      res += ch;
    }
    return res;
  }


  public static char getOperator(int[] pattern) {
    int opcode = (int)Integer.parseInt(parsePattern(pattern));

    for (int i = 0; i < shorthandOps.length; i++) {
      if (opcode == shorthandOps[i][0]) return shorthandOps[i][1];
    }

    return 0;
  }

}//Class
