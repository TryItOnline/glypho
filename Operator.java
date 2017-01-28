// This software is Public Domain.

import java.util.Stack;
import java.io.*;

public class Operator {

  char operator;
  Operator nextOperator, loopOperator = null;
  boolean jump = false;

  public static InputStream in;
  public static OutputStream out;
  public static PrintStream err;

  public static Stack loopStack = new Stack();
  public static GlyphoStack stack = new GlyphoStack();

  public Operator(char operator){
    this.operator = operator;
    if (operator == '[') loopStack.push(this);
    if (operator == ']') {
      loopOperator = (Operator)(loopStack.pop());
      loopOperator.setLoopOperator(this);
    }
  }

  public void execute() {
    execute(operator);
  }

  protected void execute(char op) {
    switch(op) {
        case 'n': break;
        case 'i': try {
                    int b = in.read();
                    if (b < 0) stack.push(0);
                    else stack.push(b);
                  }catch (IOException e) { err.println(e); }
                  break;
        case 'o': try {
                    out.write(stack.pop());
                    out.flush();
                  }catch (IOException e) { err.println(e); }
                  break;
        case 'd': stack.push(stack.peek());
                  break;
        case '\\':int v1 = stack.pop();
                  int v2 = stack.pop();
                  stack.push(v1);
                  stack.push(v2);
                  break;
        case '>': stack.pushBottom(stack.pop());
                  break;
        case '<': stack.push(stack.popBottom());
                  break;
        case '!': stack.pop();
                  break;
        case '1': stack.push(1);
                  break;
        case '+': stack.push(stack.pop() + stack.pop());
                  break;
        case '-': stack.push(-stack.pop());
                  break;
        case '*': stack.push(stack.pop() * stack.pop());
                  break;
        case 'e': execute(GlyphoInterpreter.getOperator(new int[] {stack.pop(), stack.pop(), stack.pop(), stack.pop()}));
                  break;
        case '[': jump = (stack.peek() == 0);
                  break;
        case ']': jump = (stack.peek() != 0);
                  break;
        //Debug operator which dumps the stack:
        case '&': if (! stack.empty()) err.println(stack.toString());
                  break;
    }
  }

  public void setLoopOperator(Operator loopOperator) {
    this.loopOperator = loopOperator;
  }

  public Operator next() {
    return nextOperator;
  }

  public void setNext(Operator next){
    nextOperator = next;
  }

  public Operator conditionalNext(){
    if ((loopOperator != null) && (jump)) {
      return loopOperator;
    }else{
      return nextOperator;
    }
  }

}