package fr.ul.miashs.compil.parsing;
import java_cup.runtime.Symbol;

%%
%class Scanner
%cup
%line
%column

%{
  private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn);
  }
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
  }
%}

// Expressions régulières de base
LineTerminator = \r|\n|\r\n
WhiteSpace     = {LineTerminator} | [ \t\f]
Comment        = "//" [^\r\n]* {LineTerminator}?
Identifier     = [a-zA-Z_][a-zA-Z0-9_]*
Number         = [0-9]+

%%

<YYINITIAL> {
  /* Ignorer les espaces et commentaires */
  {WhiteSpace}   { /* ignore */ }
  {Comment}      { /* ignore */ }

  /* Mots-clés */
  "function"     { return symbol(sym.FUNCTION); }
  "void"         { return symbol(sym.VOID); }
  "int"          { return symbol(sym.INT); }
  "return"       { return symbol(sym.RETURN); }
  "if"           { return symbol(sym.IF); }
  "else"         { return symbol(sym.ELSE); }
  "while"        { return symbol(sym.WHILE); }
  "write"        { return symbol(sym.WRITE); }
  "read"         { return symbol(sym.READ); }

  /* Opérateurs arithmétiques et d'affectation */
  "+"            { return symbol(sym.PLUS); }
  "-"            { return symbol(sym.MINUS); }
  "*"            { return symbol(sym.MULT); }
  "/"            { return symbol(sym.DIV); }
  "="            { return symbol(sym.ASSIGN); }

  /* Opérateurs relationnels */
  ">"            { return symbol(sym.GT); }
  "<"            { return symbol(sym.LT); }
  "<="           { return symbol(sym.LE); }
  ">="           { return symbol(sym.GE); }
  "=="           { return symbol(sym.EQ); }
  "!="           { return symbol(sym.NEQ); }

  /* Ponctuation */
  "("            { return symbol(sym.LPAREN); }
  ")"            { return symbol(sym.RPAREN); }
  "{"            { return symbol(sym.LBRACE); }
  "}"            { return symbol(sym.RBRACE); }
  ","            { return symbol(sym.COMMA); }

  /* Valeurs et Identifiants */
  {Number}       { return symbol(sym.NUMBER, Integer.valueOf(yytext())); }
  {Identifier}   { return symbol(sym.IDENTIFIER, yytext()); }
}

/* Gestion des erreurs */
[^]              { throw new Error("Caractère illégal <"+ yytext()+"> à la ligne " + yyline); }