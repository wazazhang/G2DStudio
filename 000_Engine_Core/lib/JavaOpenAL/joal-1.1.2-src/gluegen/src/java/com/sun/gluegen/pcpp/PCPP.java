/*
 * Copyright (c) 2003 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * - Redistribution of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES,
 * INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN
 * MICROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR
 * ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR
 * DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE
 * DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY,
 * ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF
 * SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed or intended for use
 * in the design, construction, operation or maintenance of any nuclear
 * facility.
 * 
 * Sun gratefully acknowledges that this software was originally authored
 * and developed by Kenneth Bradley Russell and Christopher John Kline.
 */

package com.sun.gluegen.pcpp;

import java.io.*;
import java.util.*;

/** A minimal pseudo-C-preprocessor designed in particular to preserve
    #define statements defining constants so they can be observed by a
    glue code generator. */

public class PCPP {
  private static final boolean disableDebugPrint = true;

  public PCPP(List/*<String>*/ includePaths) {
    this.includePaths = includePaths;
    setOut(System.out);
  }

  public OutputStream out()                    { return out;     }
  public void         setOut(OutputStream out) { this.out = out; writer = new PrintWriter(out); }

  public void run(Reader reader, String filename) throws IOException {
    StreamTokenizer tok = null;
    BufferedReader bufReader = null;
    if (reader instanceof BufferedReader) {
      bufReader = (BufferedReader) reader;
    } else {
      bufReader = new BufferedReader(reader);
    }
    tok = new StreamTokenizer(new ConcatenatingReader(bufReader));
    tok.resetSyntax();
    tok.wordChars('a', 'z');
    tok.wordChars('A', 'Z');
    tok.wordChars('0', '9');
    tok.wordChars('_', '_');
    tok.wordChars('.', '.');
    tok.wordChars(128 + 32, 255);
    tok.whitespaceChars(0, ' ');
    tok.quoteChar('"');
    tok.quoteChar('\'');
    tok.eolIsSignificant(true);
    tok.slashSlashComments(true);
    tok.slashStarComments(true);
    ParseState curState = new ParseState(tok, filename);
    ParseState oldState = state;
    state = curState;
    lineDirective();
    parse();
    state = oldState;
    if (state != null) {
      lineDirective();
    }
  }

  public static void main(String[] args) {
    try {
      Reader reader = null;
      String filename = null;

      if (args.length == 0) {
        usage();
      }

      List includePaths = new ArrayList();
      for (int i = 0; i < args.length; i++) {
        if (i < args.length - 1) {
          String arg = args[i];
          if (arg.startsWith("-I")) {
            String[] paths = arg.substring(2).split(System.getProperty("path.separator"));
            for (int j = 0; j < paths.length; j++) {
              includePaths.add(paths[j]);
            }
          } else {
            usage();
          }
        } else {
          String arg = args[i];
          if (arg.equals("-")) {
            reader = new InputStreamReader(System.in);
            filename = "standard input";
          } else {
            if (arg.startsWith("-")) {
              usage();
            }
            filename = arg;
            reader = new BufferedReader(new FileReader(filename));
          }
        }
      }

      new PCPP(includePaths).run(reader, filename);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String findFile(String filename) {
    String sep = File.separator;
    for (Iterator iter = includePaths.iterator(); iter.hasNext(); ) {
      String inclPath = (String) iter.next();
      String fullPath = inclPath + sep + filename;
      File file = new File(fullPath);
      if (file.exists()) {
        return fullPath;
      }
    }
    return null;
  }

  //----------------------------------------------------------------------
  // Internals only below this point
  //

  private static void usage() {
    System.out.println("Usage: java PCPP [filename | -]");
    System.out.println("Minimal pseudo-C-preprocessor.");
    System.out.println("Output goes to standard output. Standard input can be used as input");
    System.out.println("by passing '-' as the argument.");
    System.exit(1);
  }

  /** Map containing the results of #define statements. We must
      evaluate certain very simple definitions (to properly handle
      OpenGL's gl.h) but preserve the text of definitions evaluating
      to constants.  Macros and multi-line defines (which typically
      contain either macro definitions or expressions) are currently
      not handled. */
  private Map/*<String, String>*/ defineMap          = new HashMap();
  private Set/*<String>*/         nonConstantDefines = new HashSet();

  /** List containing the #include paths as Strings */
  private List/*<String>*/ includePaths;

  // State
  static class ParseState {
    private StreamTokenizer tok;
    private String          filename;
    private int             lineNumber;
    private boolean         startOfLine;
    private boolean         startOfFile;

    ParseState(StreamTokenizer tok, String filename) {
      this.tok = tok;
      this.filename = filename;
      lineNumber = 1;
      startOfLine = true;
      startOfFile = true;
    }

    StreamTokenizer tok()                       { return tok;          }
    String          filename()                  { return filename;     }
    int             lineNumber()                { return tok.lineno(); }
    boolean         startOfLine()               { return startOfLine;  }
    void            setStartOfLine(boolean val) { startOfLine = val;   }
    boolean         startOfFile()               { return startOfFile;  }
    void            setStartOfFile(boolean val) { startOfFile = val;   }
  }

  private ParseState  state;

  // Accessors

  private void pushBackToken() throws IOException {
    state.tok().pushBack();
  }

  /** Equivalent to nextToken(false) */
  private int nextToken() throws IOException {
    return nextToken(false);
  }

  private int nextToken(boolean returnEOLs) throws IOException {
    int lineno = lineNumber();
    // Check to see whether the previous call to nextToken() left an
    // EOL on the stream
    if (curToken() == StreamTokenizer.TT_EOL) {
      state.setStartOfLine(true);
    } else if (!state.startOfFile()) {
      state.setStartOfLine(false);
    }
    state.setStartOfFile(false);
    int val = state.tok().nextToken();
    if (!returnEOLs) {
      if (val == StreamTokenizer.TT_EOL) {
        do {
          // Consume and return next token, setting state appropriately
          val = state.tok().nextToken();
          state.setStartOfLine(true);
          println();
        } while (val == StreamTokenizer.TT_EOL);
      }
    }
    if (lineNumber() > lineno + 1) {
      // This is a little noisier than it needs to be, but does handle
      // the case of multi-line comments properly
      lineDirective();
    }
    return val;
  }
  
  /**
   * Reads the next token and throws an IOException if it is not the specified
   * token character.
   */
  private void nextRequiredToken(int requiredToken) throws IOException {
    int nextTok = nextToken();
    if (nextTok != requiredToken) {
      String msg = "Expected token '" + requiredToken + "' but got ";
      switch (nextTok) {
        case StreamTokenizer.TT_EOF: msg += "<EOF>"; break;
        case StreamTokenizer.TT_EOL: msg += "<EOL>"; break;    
        default: msg += "'" + curTokenAsString() + "'"; break;    
      }
      msg += " at file " + filename() + ", line " + lineNumber();
      throw new IOException(msg);
    }
  }

  private int curToken() {
    return state.tok().ttype;
  }

  private String curTokenAsString() {
    int t = curToken();
    if (t == StreamTokenizer.TT_WORD) {
      return curWord();
    }
    if (t == StreamTokenizer.TT_EOL) {
      throw new RuntimeException("Should not be converting EOL characters to strings");
    }
    char c = (char) t;
    if (c == '"' || c == '\'') {
      StringBuffer buf = new StringBuffer();
      buf.append(c);
      buf.append(state.tok().sval);
      buf.append(c);
      return buf.toString();
    }
    return new String(new char[] { c });
  }

  private String nextWord() throws IOException {
    int val = nextToken();
    if (val != StreamTokenizer.TT_WORD) {
      throw new RuntimeException("Expected word at file " + filename() +
                                 ", line " + lineNumber());
    }
    return curWord();
  }

  private String curWord() {
    return state.tok().sval;
  }

  private boolean startOfLine() {
    return state.startOfLine();
  }

  private String filename() {
    return state.filename();
  }

  private int lineNumber() {
    return state.lineNumber();
  }

  /////////////
  // Parsing //
  /////////////

  private void parse() throws IOException {
    int tok = 0;
    while ((tok = nextToken()) != StreamTokenizer.TT_EOF) {
      // A '#' at the beginning of a line is a preprocessor directive
      if (startOfLine() && (tok == '#')) {
        preprocessorDirective();
      } else {
        // Output white space plus current token, handling #defines
        // (though not properly -- only handling #defines to constants and the empty string)
        print(" ");
        String s = curTokenAsString();
        String newS = (String) defineMap.get(s);
        if (newS == null) {
          newS = s;
        }
        print(newS);
      }
    }
    flush();
  }

  private void preprocessorDirective() throws IOException {
    String w = nextWord();
    boolean shouldPrint = true;
    if (w.equals("define")) {
      handleDefine();
      shouldPrint = false;
    } else if (w.equals("undef")) {
      handleUndefine();
      shouldPrint = false;
    } else if (w.equals("if") || w.equals("elif")) {
      handleIf(w.equals("if"));
      shouldPrint = false;
    } else if (w.equals("ifdef") || w.equals("ifndef")) {
      handleIfdef(w.equals("ifdef"));
      shouldPrint = false;
    } else if (w.equals("else")) {
      handleElse();
      shouldPrint = false;
    } else if (w.equals("endif")) {
      handleEndif();
      shouldPrint = false;
    } else if (w.equals("include")) {
      handleInclude();
      shouldPrint = false;
    } else {
      // Unknown preprocessor directive (#pragma?) -- ignore
    }
    if (shouldPrint) {
      print("# ");
      printToken();
    }
  }

  ////////////////////////////////////
  // Handling of #define directives //
  ////////////////////////////////////

  private void handleUndefine() throws IOException {
    // Next token is the name of the #undef
    String name = nextWord();
    
    debugPrint(true, "#undef " + name);

    // there shouldn't be any extra symbols after the name, but just in case...
    List values = new ArrayList();
    while (nextToken(true) != StreamTokenizer.TT_EOL) {
      values.add(curTokenAsString());
    }

    if (enabled()) {
      String oldDef = (String)defineMap.remove(name);
      if (oldDef == null) {
          System.err.println("WARNING: ignoring redundant \"#undef " +
            name + "\", at \"" + filename() + "\" line " + lineNumber() +
            ": \"" + name + "\" was not previously defined");
      } else {
        // System.err.println("UNDEFINED: '" + name + "'  (line " + lineNumber() + " file " + filename() + ")");        
      }      
      nonConstantDefines.remove(name);
    }
    else System.err.println("FAILED TO UNDEFINE: '" + name + "'  (line " + lineNumber() + " file " + filename() + ")"); 
  }
  
  private void handleDefine() throws IOException {
    // Next token is the name of the #define
    String name = nextWord();
    //System.err.println("IN HANDLE_DEFINE: '" + name + "'  (line " + lineNumber() + " file " + filename() + ")");
    // (Note that this is not actually proper handling for multi-line #defines)
    List values = new ArrayList();
    while (nextToken(true) != StreamTokenizer.TT_EOL) {
      values.add(curTokenAsString());
    }
    // if we're not within an active block of code (like inside an "#ifdef
    // FOO" where FOO isn't defined), then don't actually alter the definition
    // map.
    debugPrint(true, "#define " + name);
    if (enabled())
    {
      boolean emitDefine = true;
     
      // Handle #definitions to nothing or to a constant value
      int sz = values.size();
      if (sz == 0) {
        // definition to nothing, like "#define FOO"
        String oldDef = (String)defineMap.put(name, "");
        if (oldDef != null) {
          System.err.println("WARNING: \"" + name + "\" redefined from \"" +
            oldDef + "\" to \"\"");
        }
        // We don't want to emit the define, because it would serve no purpose
        // and cause GlueGen errors (confuse the GnuCParser)
        emitDefine = false;
        //System.out.println("//---DEFINED: " + name + "to \"\"");
      } else if (sz == 1) {        
        // See whether the value is a constant
        String value = (String) values.get(0);
        if (isConstant(value)) {           
          // Value is numeric constant like "#define FOO 5".
          // Put it in the #define map
          String oldDef = (String)defineMap.put(name, value);
          if (oldDef != null) {
            System.err.println("WARNING: \"" + name + "\" redefined from \"" +
              oldDef + "\" to \"" + value + "\"");
          }
          //System.out.println("//---DEFINED: " + name + " to \"" + value + "\"");
        } else {
          // Value is a symbolic constant like "#define FOO BAR".
          // Try to look up the symbol's value
          String newValue = resolveDefine(value, true);
          if (newValue != null) {
            // Set the value to the value of the symbol.
            //
            // TO DO: Is this correct? Why not output the symbol unchanged?
            // I think that it's a good thing to see that some symbols are
            // defined in terms of others. -chris
            values.set(0, newValue);
          }
          else
          {
            // Still perform textual replacement
            defineMap.put(name, value);
            nonConstantDefines.add(name);
            emitDefine = false;
          }
        }
      }
      else
      {
        // Non-constant define; try to do reasonable textual substitution anyway
        // (FIXME: should identify some of these, like (-1), as constants)
        emitDefine = false;
        StringBuffer val = new StringBuffer();
        for (int i = 0; i < sz; i++) {
          if (i != 0) {
            val.append(" ");
          }
          val.append(resolveDefine((String) values.get(i), false));
        }
        if (defineMap.get(name) != null) {
          // This is probably something the user should investigate.
          throw new RuntimeException("Cannot redefine symbol \"" + name +
            " from \"" + defineMap.get(name) + "\" to non-constant " +
            " definition \"" + val.toString() + "\"");
        }
        defineMap.put(name, val.toString());
        nonConstantDefines.add(name);
      }        
      
      if (emitDefine)
      {
        // Print name and value
        print("# define ");
        print(name);
        for (Iterator iter = values.iterator(); iter.hasNext(); ) {
          print(" ");
          print((String) iter.next());
        }
        println();
      }

    } // end if (enabled())
    
    //System.err.println("OUT HANDLE_DEFINE: " + name);
  }

  private boolean isConstant(String s) {
    if (s.startsWith("0x") || s.startsWith("0X")) {
      return checkHex(s);
    } else {
      return checkDecimal(s);
    }
  }

  private boolean checkHex(String s) {
    for (int i = 2; i < s.length(); i++) {
      char c = s.charAt(i);
      if (!((c >= '0' && c <= '9') ||
            (c >= 'a' && c <= 'f') ||
            (c >= 'A' && c <= 'F'))) {
        return false;
      }
    }
    return true;
  }

  private boolean checkDecimal(String s) {
    try {
      Float.valueOf(s);
    }
    catch (NumberFormatException e) {
      // not parsable as a number
      return false;
    }
    return true;
  }

  private String resolveDefine(String word, boolean returnNullIfNotFound) {
    String lastWord = (String) defineMap.get(word);
    if (lastWord == null) {
      if (returnNullIfNotFound) {
        return null;
      }
      return word;
    }
    String nextWord = null;
    do {
      nextWord = (String) defineMap.get(lastWord);
      if (nextWord != null) {
        lastWord = nextWord;
      }
    } while (nextWord != null);
    return lastWord;
  }

  ////////////////////////////////////////////////
  // Handling of #if/#ifdef/ifndef/endif directives //
  ////////////////////////////////////////////////

  /**
   * @param isIfdef if true, we're processing #ifdef; if false, we're
   * processing #ifndef.
   */
  private void handleIfdef(boolean isIfdef) throws IOException {
    // Next token is the name of the #ifdef
    String symbolName = nextWord();
    debugPrint(true, (isIfdef ? "#ifdef " : "#ifndef ") + symbolName);
    boolean symbolIsDefined = defineMap.get(symbolName) != null;
    //debugPrint(true, "HANDLE_IFDEF: ifdef(" + symbolName + ") = " + symbolIsDefined );    
    pushEnableBit(enabled() && symbolIsDefined == isIfdef);    
  }

  /** Handles #else directives */
  private void handleElse() throws IOException {
    boolean enabledStatusBeforeElse = enabled();
    popEnableBit();
    pushEnableBit(enabled() && !enabledStatusBeforeElse);
    debugPrint(true, "#else ");
  }

  private void handleEndif() {
    boolean enabledBeforePopping = enabled();
    popEnableBit();

    // print the endif if we were enabled prior to popEnableBit() (sending
    // false to debugPrint means "print regardless of current enabled() state).
    debugPrint(!enabledBeforePopping, "#endif/end-else");
  }

  /**
   * @param isIf if true, we're processing #if; if false, we're
   * processing #elif.
   */
  private void handleIf(boolean isIf) throws IOException {
    //System.out.println("IN HANDLE_" + (isIf ? "IF" : "ELIF") + " file \"" + filename() + " line " + lineNumber());
    debugPrint(true, (isIf ? "#if" : "#elif"));
    boolean defineEvaluatedToTrue = handleIfRecursive(true);
    if (!isIf) {
      popEnableBit();
    }
    pushEnableBit(enabled() && defineEvaluatedToTrue == isIf);
    //System.out.println("OUT HANDLE_" + (isIf ? "IF" : "ELIF") +" (evaluated to " + defineEvaluatedToTrue + ")");
  }

  //static int tmp = -1;

  /**
   * This method is called recursively to process nested sub-expressions such as: 
   * <pre>
   *   #if !defined(OPENSTEP) && !(defined(NeXT) || !defined(NeXT_PDO))
   *</pre>
   *
   * @param greedy if true, continue evaluating sub-expressions until EOL is
   * reached. If false, return as soon as the first sub-expression is
   * processed.
   * @return the value of the sub-expression or (if greedy==true)
   * series of sub-expressions.
   */
  private boolean handleIfRecursive(boolean greedy) throws IOException {
    //System.out.println("IN HANDLE_IF_RECURSIVE (" + ++tmp + ", greedy = " + greedy + ")"); System.out.flush();
    
    // ifValue keeps track of the current value of the potentially nested
    // "defined()" expressions as we process them.
    boolean ifValue = true; 
    int openParens = 0;
    int tok;
    do {       
      tok = nextToken(true);          
      //System.out.println("-- READ: [" + (tok == StreamTokenizer.TT_EOL ? "<EOL>" :curTokenAsString()) + "]"); 
      switch (tok) {
        case '(':
          ++openParens;
          //System.out.println("OPEN PARENS = " + openParens);
          ifValue = ifValue && handleIfRecursive(true);
          break;
        case ')':
          --openParens;
          //System.out.println("OPEN PARENS = " + openParens);
          break;
        case '!':
        {
          //System.out.println("HANDLE_IF_RECURSIVE HANDLING !");
          boolean rhs = handleIfRecursive(false);
          ifValue = !rhs;
          //System.out.println("HANDLE_IF_RECURSIVE HANDLED OUT !, RHS = " + rhs);
        }
        break;
        case '&':        
        {
          nextRequiredToken('&');
          //System.out.println("HANDLE_IF_RECURSIVE HANDLING &&, LHS = " + ifValue);
          boolean rhs = handleIfRecursive(true);
          //System.out.println("HANDLE_IF_RECURSIVE HANDLED &&, RHS = " + rhs);
          ifValue = ifValue && rhs;
        }
        break;
        case '|':
        {
            nextRequiredToken('|');
            //System.out.println("HANDLE_IF_RECURSIVE HANDLING ||, LHS = " + ifValue);
            boolean rhs = handleIfRecursive(true);
            //System.out.println("HANDLE_IF_RECURSIVE HANDLED ||, RHS = " + rhs);
            ifValue = ifValue || rhs;
        }
        break;
        case '>':
        {
            // NOTE: we don't handle expressions like this properly
            boolean rhs = handleIfRecursive(true);
            ifValue = false;
        }
        break;
        case '<':
        {
            // NOTE: we don't handle expressions like this properly
            boolean rhs = handleIfRecursive(true);
            ifValue = false;
        }
        break;
        case '=':
        {
            // NOTE: we don't handle expressions like this properly
            boolean rhs = handleIfRecursive(true);
            ifValue = false;
        }
        break;
        case StreamTokenizer.TT_WORD: 
        {
          String word = curTokenAsString();
          if (word.equals("defined")) {
            // Handle things like #if defined(SOMESYMBOL)
            nextRequiredToken('(');
            String symbol = nextWord();
            boolean isDefined = defineMap.get(symbol) != null;
            //System.out.println("HANDLE_IF_RECURSIVE HANDLING defined(" + symbol + ") = " + isDefined);
            ifValue = ifValue && isDefined;
            nextRequiredToken(')');
          }
          else {
            // Handle things like #if SOME_SYMBOL.
            String symbolValue = (String)defineMap.get(word);

            // See if the statement is "true"; i.e., a non-zero expression
            if (symbolValue != null) {
              // The statement is true if the symbol is defined and is a constant expression
              return (!nonConstantDefines.contains(word));
            } else {
              // The statement is true if the symbol evaluates to a non-zero value
              // 
              // NOTE: This doesn't yet handle evaluable expressions like "#if
              // SOME_SYMBOL > 5" or "#if SOME_SYMBOL == 0", both of which are
              // valid syntax. It only handles numeric symbols like "#if 1"

              try {
                // see if it's in decimal form
                return Double.parseDouble(word) != 0;
              }
              catch (NumberFormatException nfe1) {
                try {
                  // ok, it's not a valid decimal value, try hex/octal value
                  return Long.parseLong(word) != 0;
                }
                catch (NumberFormatException nfe2) {
                  try {
                    // ok, it's not a valid hex/octal value, try boolean
                    return Boolean.valueOf(word) == Boolean.TRUE;
                  }
                  catch (NumberFormatException nfe3) {
                    // give up; the symbol isn't a numeric or boolean value
                    return false;
                  }
                }
              }
            }     
          } 
        } // end case TT_WORD
        break;
        case StreamTokenizer.TT_EOL:
          //System.out.println("HANDLE_IF_RECURSIVE HIT <EOL>!");
          pushBackToken(); // so caller hits EOL as well if we're recursing
          break; 
        case StreamTokenizer.TT_EOF: 
          throw new RuntimeException("Unexpected end of file while parsing " +
            "#if statement at file " + filename() + ", line " + lineNumber());        
        
        default:
          throw new RuntimeException("Unexpected token (" + curTokenAsString() +
            ") while parsing " + "#if statement at file " + filename() +
            ", line " + lineNumber());        
      }
      //System.out.println("END OF WHILE: greedy = " + greedy + " parens = " +openParens + " not EOL = " + (tok != StreamTokenizer.TT_EOL) + " --> " + ((greedy && openParens >= 0) && tok != StreamTokenizer.TT_EOL));
    } while ((greedy && openParens >= 0) && tok != StreamTokenizer.TT_EOL);
    //System.out.println("OUT HANDLE_IF_RECURSIVE (" + tmp-- + ", returning " + ifValue + ")");
    //System.out.flush();
    return ifValue;
  }
  
  /////////////////////////////////////
  // Handling of #include directives //
  /////////////////////////////////////
    
  private void handleInclude() throws IOException {
    // Two kinds of #includes: one with quoted string for argument,
    // one with angle brackets surrounding argument
    int t = nextToken();
    String filename = null;
    if (t == '"') {
      filename = curWord();
    } else if (t == '<') {
      // Components of path name are coming in as separate tokens;
      // concatenate them
      StringBuffer buf = new StringBuffer();
      while ((t = nextToken()) != '>' && (t != StreamTokenizer.TT_EOF)) {
        buf.append(curTokenAsString());
      }
      if (t == StreamTokenizer.TT_EOF) {
        System.err.println("WARNING: unexpected EOF while processing #include directive");
      }
      filename = buf.toString();
    }
    // if we're not within an active block of code (like inside an "#ifdef
    // FOO" where FOO isn't defined), then don't actually process the
    // #included file.
    debugPrint(true, "#include [" + filename + "]");
    if (enabled())
    {
      // Look up file in known #include path
      String fullname = findFile(filename);
      //System.out.println("ACTIVE BLOCK, LOADING " + filename);
      if (fullname == null) {
        System.err.println("WARNING: unable to find #include file \"" + filename + "\"");
        return;
      }      
      // Process this file in-line
      Reader reader = new BufferedReader(new FileReader(fullname));
      run(reader, fullname);
    }
    else
    {
      //System.out.println("INACTIVE BLOCK, SKIPPING " + filename);      
    }
  }

  ////////////
  // Output //
  ////////////

  private OutputStream out;
  private PrintWriter  writer;
  private ArrayList    enabledBits = new ArrayList();
  
  private static int debugPrintIndentLevel = 0;
  private void debugPrint(boolean onlyPrintIfEnabled, String msg)
  {
    if (disableDebugPrint) {
      return;
    }
    
    if (!onlyPrintIfEnabled || (onlyPrintIfEnabled && enabled()))
    {
      for (int i = debugPrintIndentLevel; --i >0; ) {
        System.out.print("  ");
      }
      System.out.println(msg + "  (line " + lineNumber() + " file " + filename() + ")");
    }
  }

  private void pushEnableBit(boolean enabled) {
    enabledBits.add(new Boolean(enabled));
    ++debugPrintIndentLevel;
    //debugPrint(false, "PUSH_ENABLED, NOW: " + enabled());
  }

  private void popEnableBit() {
    if (enabledBits.size() == 0) {
      System.err.println("WARNING: mismatched #ifdef/endif pairs");
      return;
    }
    enabledBits.remove(enabledBits.size() - 1);
    --debugPrintIndentLevel;
    //debugPrint(false, "POP_ENABLED, NOW: " + enabled());
  }

  private boolean enabled() {
    return (enabledBits.size() == 0 ||
            ((Boolean) enabledBits.get(enabledBits.size() - 1)).booleanValue());
  }

  private void print(String s) {
    if (enabled()) {
      writer.print(s);
      //System.out.print(s);//debug
    }
  }
  
  private void print(char c) {
    if (enabled()) {
      writer.print(c);
      //System.err.print(c); //debug
    }
  }

  private void println() {
    if (enabled()) {
      writer.println();
      //System.err.println();//debug
    }
  }

  private void printToken() {
    print(curTokenAsString());
  }

  private void flush() {
    if (enabled()) {
      writer.flush();
      //System.err.flush(); //debug
    }
  }

  private void lineDirective() {
    print("# " + lineNumber() + " \"" + filename() + "\"");
    println();
  }
}
