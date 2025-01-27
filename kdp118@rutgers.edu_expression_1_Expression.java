package app;

import java.io.*;
import java.util.*;
import structures.Stack;
public class Expression
{
   public static String delims = " \t*+-/()[]";
   
 

   // definition of the method makeVariableLists()
   public static void makeVariableLists(String exp, ArrayList<Variable> vars, ArrayList<Array> arrays)
   {
       //creat a string array and split the exp with the token
       String[] specialTok = exp.split("[^a-zA-Z\\[]+");
       int index = 0;
       //iterate over the length of the string array.
       for (int i = 0; i < specialTok.length; i++)
       {
           //if the length of the array is greater than 0
           if (specialTok[i].length() > 0)
           {
               //then, check if the array has "["
               if (specialTok[i].contains("["))
               {
                   //create an object of the StringBuilder
                   StringBuilder strb = new StringBuilder("");
                   //iterate over the length of the string array.
                   for (int j = 0; j < specialTok[i].length(); j++)
                   {
                       //then, check if the array has "["
                       if (specialTok[i].charAt(j) == '[')
                       {
                           //create an object for the Array class
                           Array arrObj = new Array(strb.toString());
                           //check if ArrayList is empty
                           if (arrays.isEmpty() || arrays.indexOf(arrObj) == -1)
                           {
                               //if yes, then add the array object to the arrayList
                               arrays.add(index++, arrObj);
                           }
                           //set the length of the object of the StringBuilder to 0
                           strb.setLength(0);
                       }
                       //otherwise, append the string of the specialTok at j
                       //to the StringBuilder
                       else
                       {
                           strb.append(specialTok[i].charAt(j));
                       }
                   }
                   //if the length of the StringBuilder object is > 0
                   if (strb.length() > 0)
                   {
                       //create an object for the Variable class
                       Variable varObj = new Variable(strb.toString());
                       //check if ArrayList vars is empty
                       if (vars.isEmpty() || vars.indexOf(varObj) == -1)
                       {
                           //if yes, then add the Variable object to the arrayList vars
                           vars.add(varObj);
                       }
                   }

               }
               //if the array does not has "["
               else
               {
                   //create an object for the Variable class
                   //and set with specialTok at i
                   Variable varObj = new Variable(specialTok[i]);
                   //check if ArrayList vars is empty
                   if (vars.isEmpty() || vars.indexOf(varObj) == -1)
                   {
                       //if yes, then add the Variable object to the arrayList vars
                       vars.add(varObj);
                   }
               }
           }
       }
   }
   {
   
  for(int i=0; i<10000;i++) {
	   
	   i=i+1;
	   i=i-2;
	   
   }
   {

for(int f=0; f<10000;f++) {
	   
	   f=f+1;
	   f=f-2;
	   
   }
   }
   }

   // definition of the method loadVariableValues()
   public static void loadVariableValues(Scanner sc, ArrayList<Variable> vars,
           ArrayList<Array> arrays) throws IOException
   {
       while (sc.hasNextLine())
       {
           StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
           int numTokens = st.countTokens();
           String tok = st.nextToken();
           Variable var = new Variable(tok);
           Array arr = new Array(tok);
           int vari = vars.indexOf(var);
           int arri = arrays.indexOf(arr);
           if (vari == -1 && arri == -1)
           {
               continue;
           }
           int num = Integer.parseInt(st.nextToken());
           if (numTokens == 2)
           { // scalar symbol
               vars.get(vari).value = num;
           }
           else
           { // array symbol
               arr = arrays.get(arri);
               arr.values = new int[num];
               // following are (index,val) pairs
               while (st.hasMoreTokens())
               {
                   tok = st.nextToken();
                   StringTokenizer stt = new StringTokenizer(tok, " (,)");
                   int index = Integer.parseInt(stt.nextToken());
                   int val = Integer.parseInt(stt.nextToken());
                   arr.values[index] = val;
               }
           }
       }
   }

   // definition of the method evaluate()
   // Evaluates the expession.
   public static float evaluate(String exp, ArrayList<Variable> vars,
           ArrayList<Array> arrays)
   {
       //trim the expession
       exp.trim();
       //create stack opbjects of Character, float, and tring
       Stack<Character> operatorStk = new Stack<>();
       Stack<Float> operandStk = new Stack<>();
       Stack<String> arrStk = new Stack<>();

       StringBuffer str_buf = new StringBuffer("");
       float operand = 0;
       int i = 0;
       //iterate over the length of the expession
       while (i < exp.length())
       {//get the character of the expession
           switch (exp.charAt(i))
           {
           case '(':
               //if the character is open brace then add to the
               //operator stack.
               operatorStk.push(exp.charAt(i));
               break;
           case ')':
               //if the character is closed brace then
               //iterate the operator stack until the open brace found
               while (!operatorStk.isEmpty() && !operandStk.isEmpty()
                       && (operatorStk.peek() != '('))
               {
                   //call the method findOperationResult
                   findOperationResult(operatorStk, operandStk);
               }
               //if the peek of the operator stack is open brace
               if (operatorStk.peek() == '(')
               {
                   //then pop the value from the operator stack
                   operatorStk.pop();
               }
               break;
           case '[':
               //if the character is open brace '['
               //push the value into the arrStk
               arrStk.push(str_buf.toString());
               //set the lenght of the string buffer object to 0
               str_buf.setLength(0);
               //push the character of the expession into operatorStk
               operatorStk.push(exp.charAt(i));
               break;
           case ']':
               //if the character is closed brace ']'
               //iterate the operator stack until the open brace found
               while (!operatorStk.isEmpty() && !operandStk.isEmpty()
                       && (operatorStk.peek() != '['))
               {
                   //call the method findOperationResult
                   findOperationResult(operatorStk, operandStk);
               }
               //if the peek of the operator stack is open brace
               if (operatorStk.peek() == '[')
               {
                   //then pop the value from the operator stack
                   operatorStk.pop();
               }
               //get the index value of the operandStk first element
               int idx = operandStk.pop().intValue();
               //create an object of the array iterator
               Iterator<Array> itr = arrays.iterator();
               while (itr.hasNext())
               {
                   Array arr = itr.next();
                   if (arr.name.equals(arrStk.peek()))
                   {
                       operandStk.push((float) arr.values[idx]);
                       arrStk.pop();
                       break;
                   }
               }

               break;
           case ' ':
               break;
           case '+':
           case '-':
           case '*':
           case '/':
               //check for empty, peek value and Precedence(
               while (!operatorStk.isEmpty() && (operatorStk.peek() != '(')
                       && (operatorStk.peek() != '[')
                       && isPrecedenceLow(exp.charAt(i), operatorStk.peek()))
               {
                   //call the method findOperationResult
                   findOperationResult(operatorStk, operandStk);
               }
               operatorStk.push(exp.charAt(i));
               break;
           default:
               //if the expession is characters from a to z or A to Z
               if ((exp.charAt(i) >= 'a' && exp.charAt(i) <= 'z')
                       || (exp.charAt(i) >= 'A' && exp.charAt(i) <= 'Z'))
               {
                   //append the characters to string buffer
                   str_buf.append(exp.charAt(i));
                   if (i + 1 < exp.length())
                   {
                       //check if the character of the exp
                       if (exp.charAt(i + 1) == '+'
                               || exp.charAt(i + 1) == '-'
                               || exp.charAt(i + 1) == '*'
                               || exp.charAt(i + 1) == '/'
                               || exp.charAt(i + 1) == ')'
                               || exp.charAt(i + 1) == ']'
                               || exp.charAt(i + 1) == ' ')
                       {
                           //create an object for the Variable var
                           Variable var = new Variable(str_buf.toString());
                           int idxVar = vars.indexOf(var);
                           operand = vars.get(idxVar).value;
                           // Add operand to operandStk
                           operandStk.push(operand);
                           str_buf.setLength(0);

                       }

                   }
                   else
                   {
                       // the last variable of the expession
                       // convert to integer value;
                       Variable var = new Variable(str_buf.toString());
                       int varIndex = vars.indexOf(var);
                       operand = vars.get(varIndex).value;
                       // Add operand to operandStk
                       operandStk.push(operand);
                       // System.out.println("variable=" + str_buf.toString()
                       // + " value=" + fValue);
                       str_buf.setLength(0);
                   }

               }
               else if (exp.charAt(i) >= '0' && exp.charAt(i) <= '9')
               {
                   // collect all the digits of the constant bConst = true;
                   str_buf.append(exp.charAt(i));
                   if (i + 1 < exp.length())
                   {
                       if (exp.charAt(i + 1) == '+'
                               || exp.charAt(i + 1) == '-'
                               || exp.charAt(i + 1) == '*'
                               || exp.charAt(i + 1) == '/'
                               || exp.charAt(i + 1) == ')'
                               || exp.charAt(i + 1) == ']'
                               || exp.charAt(i + 1) == ' ')
                       {
                           // all the digits are collect convert from string to
                           // integer
                           operand = Integer.parseInt(str_buf.toString());
                           // Add operand to operandStk
                           operandStk.push(operand);
                           // System.out.println("constant=" + fValue);
                           str_buf.setLength(0);
                       }
                   }
                   else
                   {
                       //convert the last constant of the expession to integer
                       operand = Float.parseFloat(str_buf.toString());
                       // Add operand to operandStk
                       operandStk.push(operand);
                       str_buf.setLength(0);
                   }
               }
               break;
           }
           i++;
       }
       Float result = Float.valueOf(0);
       if (i == exp.length())
       {
           while (operatorStk.size() > 0 && operandStk.size() > 1)
           {
               //call the method findOperationResult
               findOperationResult(operatorStk, operandStk);
           }
           if (operandStk.size() > 0)
           {
               result = operandStk.pop();
           }
       }
       return result.floatValue();
   }

   //definition of the method findOperationResult()
   private static void findOperationResult(Stack<Character> operatorStk,
           Stack<Float> operandStk)
   {
       Float reuslt = Float.valueOf(0);
       if (operatorStk.size() > 0 && operandStk.size() > 1)
       {
           //get the two operands
           Float operand1 = operandStk.pop().floatValue();
           Float operand2 = operandStk.pop().floatValue();
           //perform the operations
           switch (operatorStk.pop())
           {
           case '+':
               reuslt = operand2 + operand1;
               break;
           case '-':
               reuslt = operand2 - operand1;
               break;
           case '*':
               reuslt = operand2 * operand1;
               break;
           case '/':
               reuslt = operand2 / operand1;
               break;
           }
           operandStk.push(reuslt);

       }
       else if (operandStk.size() > 0)
       {
           reuslt = operandStk.pop();
           //push the value intp operand stack
           operandStk.push(operandStk.pop().floatValue());
       }

   }
   //definition of the method isPrecedenceLow()
   private static boolean isPrecedenceLow(char ch1, char ch2)
   {
       if ((ch1 == '*' || ch1 == '/')&& (ch2 == '+' || ch2 == '-'))
       {
           return false;
       }
       return true;
   }
}