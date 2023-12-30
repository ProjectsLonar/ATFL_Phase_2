package com.lonar.atflMobileInterfaceService.common;



import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

public class CSVWriter {
	
public CSVWriter(PrintWriter pw, boolean forceQuotes, char separator, String lineSeparator) {
   this.pw = pw;
   this.forceQuotes = forceQuotes;
   this.separator = separator;
   this.comment = "# ";
   this.lineSeparator = lineSeparator;
} // end of CSVWriter

public CSVWriter(Writer w, boolean forceQuotes, char separator, String lineSeparator) {
    this(new PrintWriter(w),forceQuotes,separator,lineSeparator);
}
public CSVWriter(PrintWriter pw) {
   this.pw = pw;
   this.forceQuotes = false;
   this.separator = ',';
   this.comment = "# ";
   this.lineSeparator = System.getProperty("line.separator");
} 

 
public CSVWriter(Writer w) {
    this(new PrintWriter(w));
}
public CSVWriter(PrintWriter pw, char comment) {
   this.pw = pw;
   this.forceQuotes = false;
   this.separator = ',';
   this.comment = String.valueOf(comment) + " ";
   this.lineSeparator = System.getProperty("line.separator");
} // end of CSVWriter

public CSVWriter(Writer w, char comment) {
    this(new PrintWriter(w),comment);
}

PrintWriter pw;

boolean forceQuotes;
char separator;
boolean wasPreviousField = false;

String comment;

String lineSeparator;

public void writeCommentln(String text) {
   if (wasPreviousField) writeln(); // close open line since we need to start a new one for comment
   pw.print(comment);
   //wasPreviousField = false; // to prevent a comma after the comment sign
   write(text);
   writeln();
} // end of writeComentln

/**
 * Writes a single value in a line suited by a newline to the file given by the <code>token</code>.
 * @param token contains the value.
 */
public void writeln(String token) {
   write(token);
   writeln();
} // end of writeln

/**
 * Writes a new line in the CVS output file to demark the end of record.
 */
public void writeln() {
   /* don't bother to write last pending comma on the line */
   wasPreviousField = false;
   pw.print(lineSeparator);
} // end of writeln

/**
 * Writes a single line of comma separated values from the array given by <code>line</code>.
 * @param line containig an array of tokens.
 */
public void writeln(String[] line) {
   for(int ii=0; ii < line.length; ii++) {
      write(line[ii]);
   } // end of for

   writeln(); // write newLine

} // end of writeln

/**
  * Write one csv field to the file, followed by a separator
  * unless it is the last field on the line. Lead and trailing
  * blanks will be removed.
  *
  * @param s      The string to write.  Any additional quotes or
  *               embedded quotes will be provided by write.
  */
public void write(String s) {
   if ( wasPreviousField ) {
      pw.print(separator);
   }

   if ( s == null ) {
      pw.print("");
      return;
   } // end of if s == null

   s = s.trim();
   if ( s.indexOf('\"') >= 0 ) {
      /* worst case, needs surrounding quotes and internal quotes doubled */
      pw.print ('\"');
      for ( int i=0; i<s.length(); i++ ) {
         char c = s.charAt(i);
         if ( c == '\"' ) {
            pw.print("\"\"");
         } else {
            pw.print(c);
         }
      }
      pw.print ('\"');
      // end of if \"
   } else if ( s.indexOf('\n') >=0 ) {
      // bad case as well: having a new line in the token: \n
      pw.print ('\"');
      for ( int i=0; i<s.length(); i++ ) {
         char c = s.charAt(i);
         if ( c == '\n' ) {
            pw.print("\\n");
         } else {
            pw.print(c);
         }
      }
      pw.print ('\"');
      // end of if \n
   } else if ( forceQuotes || s.indexOf(separator) >= 0 ) {
      /* need surrounding quotes */
      pw.print ('\"');
      pw.print(s);
      pw.print ('\"');
   } else {
      /* ordinary case, no surrounding quotes needed */
      pw.print(s);
   }
   /* make a note to print trailing comma later */
   wasPreviousField = true;
} // end of write

/**
 * Close the PrintWriter.
 */
public void close() {
   if ( pw != null ) {
      pw.close();
      pw = null;
   } // end of if
} // end of close

/**
 * Test driver
 *
 * @param args  [0]: The name of the file.
 */
static public void main(String[] args) {
   try {
      // write out a test file
	   String csvFile = "E:\\ATFL\\ATFLUATData\\developer.csv";
	   File file=new File(csvFile);
      PrintWriter pw = new PrintWriter( new FileWriter(file));
      CSVWriter csv = new CSVWriter(pw, false, ',', System.getProperty("line.separator") );
      csv.writeCommentln("This is a test csv-file: '" + args[0] + "'");
      csv.write("abc");
      csv.write("def");
      csv.write("g h i");
      csv.write("jk,l");
      csv.write("m\"n\'o ");
      csv.writeln();
      csv.write("m\"n\'o ");
      csv.write("    ");
      csv.write("a");
      csv.write("x,y,z");
      csv.write("x;y;z");
      csv.writeln();
      csv.writeln(new String[] {"This, hi", "is", "an", "array."});
      csv.close();
   } catch ( IOException  e ) {
      e.printStackTrace();
      System.out.println(e.getMessage());
   }
} // end main




} // end CSVWriter

//end of file


 
 