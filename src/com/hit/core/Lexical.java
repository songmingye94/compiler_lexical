package com.hit.core;

import java.util.*;



public class Lexical{
	public void Lexical_Analysis(String source){
                cur_index = 0;
                cur_row = 1;
                Token_List.clear();
                Lexical_Error_List.clear();
		char cur_char;
		while((cur_char = getNext(source))!=0){
			if(cur_char==' ' || cur_char=='\t'){//空格或者制表符忽略
				
			}else if(cur_char=='\n' || cur_char=='\r'){//回车换行
				cur_row++;
			}else if((cur_char<='Z' && cur_char>='A')||(cur_char<='z' && cur_char>='a')){//标示符关键字
				String temp_string = cur_char+"";
				while((cur_char = getNext(source))!=0){
					if((cur_char<='Z' && cur_char>='A')||(cur_char<='z' && cur_char>='a')||(cur_char<='9' && cur_char>='0')||cur_char=='_'){
						temp_string += cur_char;
					}else{
						cur_index--;
						break;
					}
				}
				Token temp_token = new Token();
				if(Key_Word_List.contains(temp_string)){
					temp_token.code = Token_Code_List.indexOf(temp_string);
					temp_token.value = temp_string;
					temp_token.row_number = cur_row;
                                        temp_token.true_value = null;
				}else{
					temp_token.code = Token_Code_List.indexOf("id");
					temp_token.value = temp_string;
					temp_token.row_number = cur_row;
                                        if(Sign_List_Temp.contains(temp_string)){
                                            temp_token.true_value = Sign_List_Temp.indexOf(temp_string)+"";
                                            //Sign_List_Temp.add(temp_string);
                                            
                                        }else{
                                            temp_token.true_value = Sign_List_Temp.size()+"";
                                            Sign_List_Temp.add(temp_string);
                                            Sign_List.add(temp_token);
                                        }
				}
				Token_List.add(temp_token);
			}else if(cur_char<='9' && cur_char>='0'){//整型或浮点型常量
				String temp_string = cur_char+"";
				while((cur_char = getNext(source))!=0){
					if(cur_char<='9' && cur_char>='0'){
						temp_string += cur_char;
					}else{
						//cur_index--;
						break;
					}
				}
				if(cur_char=='.'){
					temp_string += cur_char;
					while((cur_char = getNext(source))!=0){
						if(cur_char<='9' && cur_char>='0'){
							temp_string += cur_char;
						}else{
							cur_index--;
							break;
						}
					}
					Token temp_token = new Token();
					temp_token.code = Token_Code_List.indexOf("const real");
					temp_token.value = temp_string;
					temp_token.row_number = cur_row;
                                        temp_token.true_value = temp_string;
					Token_List.add(temp_token);
				}else{
					//if(cur_char!=0){
						cur_index--;
					//}
					Token temp_token = new Token();
					temp_token.code = Token_Code_List.indexOf("const int");
					temp_token.value = temp_string;
					temp_token.row_number = cur_row;
                                        temp_token.true_value = temp_string;
					Token_List.add(temp_token);
				}
			}else if(cur_char=='\"'){//文本字符串
				String temp_string="";
				while((cur_char = getNext(source))!=0){
					if(cur_char=='\\'){
						cur_char = getNext(source);
						if(!(Escape_Character_List.contains(cur_char+""))){
							Lexical_Error temp_error = new Lexical_Error();
							temp_error.code = 3;
							temp_error.row_number = cur_row;
							Lexical_Error_List.add(temp_error);
							temp_string += cur_char;
						}else{
							temp_string += Escape_Character_Temp[Escape_Character_List.indexOf(cur_char+"")];
						}
					}else if(cur_char == '\"'){
						Token temp_token = new Token();
						temp_token.code = Token_Code_List.indexOf("text");
						temp_token.value = temp_string;
						temp_token.row_number = cur_row;
                                                temp_token.true_value = temp_string;
						Token_List.add(temp_token);
						break;
					}else{
						temp_string += cur_char;
					}
				}
				if(cur_char!='\"'){
					Lexical_Error temp_error = new Lexical_Error();
					temp_error.code = 4;
					temp_error.row_number = cur_row;
					Lexical_Error_List.add(temp_error);
				}
			}else if(cur_char=='\''){ //字符常量
				String temp_string = "";
				cur_char = getNext(source);
				if(cur_char=='\\'){
					cur_char = getNext(source);
					if(!(Escape_Character_List.contains(cur_char+""))){
						Lexical_Error temp_error = new Lexical_Error();
						temp_error.code = 3;
						temp_error.row_number = cur_row;
						Lexical_Error_List.add(temp_error);
						temp_string += cur_char;
					}else{
						temp_string += Escape_Character_Temp[Escape_Character_List.indexOf(cur_char+"")];
					}
				}else if(cur_char!='\'' && cur_char!='\"' && cur_char!='\\'){
					temp_string += cur_char;
				}else{
                                    Lexical_Error temp_error = new Lexical_Error();
				    temp_error.code = 1;
				    temp_error.row_number = cur_row;
				    Lexical_Error_List.add(temp_error);
                                }
				cur_char = getNext(source);
				if(cur_char=='\''){
					Token temp_token = new Token();
					temp_token.code = Token_Code_List.indexOf("character");
					temp_token.value = temp_string;
					temp_token.row_number = cur_row;
                                        temp_token.true_value = null;
					Token_List.add(temp_token);
				}else{
					Lexical_Error temp_error = new Lexical_Error();
					temp_error.code = 1;
					temp_error.row_number = cur_row;
					Lexical_Error_List.add(temp_error);
					cur_index--;
				}
			}else if(cur_char=='/'){ //除号、注释
				cur_char = getNext(source);
				if(cur_char=='/'){//C++型注释
					while((cur_char = getNext(source))!=0){
						if(cur_char=='\n' || cur_char=='\r'){
                                                    cur_row++;
							break;
						}
					}
				}else if(cur_char=='*'){//C型注释
					while((cur_char = getNext(source))!=0){
						if(cur_char=='*'){
							cur_char = getNext(source);
							if(cur_char=='/'){
								break;
							}else{
								cur_index--;
							}
						}
					}
					if(cur_char!='/'){
						Lexical_Error temp_error = new Lexical_Error();
						temp_error.code = 0;
						temp_error.row_number = cur_row;
						Lexical_Error_List.add(temp_error);
					}
				}else{
					cur_index--;
					Token temp_token = new Token();
					temp_token.code = Token_Code_List.indexOf("/");
					temp_token.value = "/";
					temp_token.row_number = cur_row;
                                        temp_token.true_value = null;
					Token_List.add(temp_token);
				}
			}else if(cur_char=='<'){
				cur_char = getNext(source);
				if(cur_char=='='){
					Token temp_token = new Token();
					temp_token.code = Token_Code_List.indexOf("<=");
					temp_token.value = "<=";
					temp_token.row_number = cur_row;
                                        temp_token.true_value = null;
					Token_List.add(temp_token);
				}else if(cur_char=='>'){
					Token temp_token = new Token();
					temp_token.code = Token_Code_List.indexOf("<>");
					temp_token.value = "<>";
					temp_token.row_number = cur_row;
                                        temp_token.true_value = null;
					Token_List.add(temp_token);
				}else{
					cur_index--;
					Token temp_token = new Token();
					temp_token.code = Token_Code_List.indexOf("<");
					temp_token.value = "<";
					temp_token.row_number = cur_row;
                                        temp_token.true_value = null;
					Token_List.add(temp_token);
				}
			}else if(cur_char=='>'){
				cur_char = getNext(source);
				if(cur_char=='='){
					Token temp_token = new Token();
					temp_token.code = Token_Code_List.indexOf(">=");
					temp_token.value = ">=";
					temp_token.row_number = cur_row;
                                        temp_token.true_value = null;
					Token_List.add(temp_token);
				}else{
					cur_index--;
					Token temp_token = new Token();
					temp_token.code = Token_Code_List.indexOf(">");
					temp_token.value = ">";
					temp_token.row_number = cur_row;
                                        temp_token.true_value = null;
					Token_List.add(temp_token);
				}
			}else if(cur_char=='='){
				cur_char = getNext(source);
				if(cur_char=='='){
					Token temp_token = new Token();
					temp_token.code = Token_Code_List.indexOf("==");
					temp_token.value = "==";
					temp_token.row_number = cur_row;
                                        temp_token.true_value = null;
					Token_List.add(temp_token);
				}else{
					cur_index--;
					Token temp_token = new Token();
					temp_token.code = Token_Code_List.indexOf("=");
					temp_token.value = "=";
					temp_token.row_number = cur_row;
                                        temp_token.true_value = null;
					Token_List.add(temp_token);
				}
			}else if(cur_char=='+'){
				cur_char = getNext(source);
				if(cur_char=='+'){
					Token temp_token = new Token();
					temp_token.code = Token_Code_List.indexOf("++");
					temp_token.value = "++";
					temp_token.row_number = cur_row;
                                        temp_token.true_value = null;
					Token_List.add(temp_token);
				}else{
					cur_index--;
					Token temp_token = new Token();
					temp_token.code = Token_Code_List.indexOf("+");
					temp_token.value = "+";
					temp_token.row_number = cur_row;
                                        temp_token.true_value = null;
					Token_List.add(temp_token);
				}
			}else if(cur_char=='-'){
				cur_char = getNext(source);
				if(cur_char=='-'){
					Token temp_token = new Token();
					temp_token.code = Token_Code_List.indexOf("--");
					temp_token.value = "--";
					temp_token.row_number = cur_row;
                                        temp_token.true_value = null;
					Token_List.add(temp_token);
				}else{
					cur_index--;
					Token temp_token = new Token();
					temp_token.code = Token_Code_List.indexOf("-");
					temp_token.value = "-";
					temp_token.row_number = cur_row;
                                        temp_token.true_value = null;
					Token_List.add(temp_token);
				}
			}else if(Token_Code_List.contains(cur_char+"")){
				Token temp_token = new Token();
				temp_token.code = Token_Code_List.indexOf(cur_char+"");
				temp_token.value = cur_char+"";
				temp_token.row_number = cur_row;
                                temp_token.true_value = null;
				Token_List.add(temp_token);
			}else{
				Lexical_Error temp_error = new Lexical_Error();
				temp_error.code = 2;
				temp_error.row_number = cur_row;
				Lexical_Error_List.add(temp_error);
			}
		}
	}
	public static char getNext(String source){//取字符串下一个
		if(cur_index >= source.length()){
			cur_index++;
			return 0;
		}else{
			char c = source.charAt(cur_index++);
			return c;
		}
	}
	private static int cur_index;//字符串下标位置
	private static int cur_row;//当前行数
	public ArrayList<Token> Token_List = new ArrayList<>();//词素识别表
	public ArrayList<Lexical_Error> Lexical_Error_List = new ArrayList<>();
        public ArrayList<Token> Sign_List = new ArrayList<>();//符号表
        public ArrayList<String> Sign_List_Temp = new ArrayList<>();
	public static  String[] Token_Code = {"if","else","for","do","while","return",
		"int","float","char","double","boolean","void","true","false","include","string","<",">","=","<=",">=","<>","==",
		"*","\\","+","-","/",";","!","character","text","id","const int",
		"const real","(",")","{","}","&","|","~",".","#","++","--","%"};//token种别码表
	public static String[] Key_Word = {"if","else","for","do","while","return",
		"int","float","char","double","boolean","void","true","false","include","string"};//关键字表
	public static String[] Lexical_Error_State = {"注释未封闭","不合法的字符常量","非法字符","非法的转义字符","字符串未封闭"};//词法错误表
	public static String[] Escape_Character = {"\'","\\","\"","r","n","t","b","f"};//转义字符表
	public static char[] Escape_Character_Temp = {'\'','\\','\"','\r','\n','\t','\b','\f'};
	private final List<String> Token_Code_List = Arrays.asList(Token_Code);
	private final List<String> Key_Word_List = Arrays.asList(Key_Word);
	private final  List<String> Lexical_Error_State_List = Arrays.asList(Lexical_Error_State);
	private final List<String> Escape_Character_List = Arrays.asList(Escape_Character);
}