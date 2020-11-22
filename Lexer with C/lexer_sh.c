#define _CRT_SECURE_NO_WARNINGS 

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>

struct Token {
	char nm[20];
	int num;
};

int get_Token(char* input_ch)
{
	struct Token t[100] = { 0, };
	strcpy(t[0].nm, "//"); t[0].num = 0;
	strcpy(t[1].nm, "int"); t[1].num = 1;
	strcpy(t[2].nm, "Id"); t[2].num = 2;
	strcpy(t[3].nm, ";"); t[3].num = 3;
	strcpy(t[4].nm, "="); t[4].num = 4;
	strcpy(t[5].nm, "IntLiteral"); t[5].num = 5;
	strcpy(t[6].nm, "+"); t[6].num = 6;
	strcpy(t[7].nm, "FloatLiteral"); t[7].num = 7;
	strcpy(t[8].nm, "if"); t[8].num = 8;
	strcpy(t[9].nm, "("); t[9].num = 9;
	strcpy(t[10].nm, "<="); t[10].num = 10;
	strcpy(t[11].nm, ")"); t[11].num = 11;
	strcpy(t[12].nm, "Variable"); t[12].num = 12;
	strcpy(t[13].nm, "=="); t[13].num = 13;
	strcpy(t[14].nm, "++"); t[14].num = 14;
	strcpy(t[15].nm, "{"); t[15].num = 15;
	strcpy(t[16].nm, "*"); t[16].num = 16;
	strcpy(t[17].nm, "<"); t[17].num = 17;
	strcpy(t[18].nm, "for"); t[18].num = 18;

	int nm_line = 1;  //row
	int num1 = 100;    //col
	char** in_arr;

	for (int i = 0; i < strlen(input_ch); i++) {
		if (input_ch[i] == 10)
			nm_line++;
	}
	in_arr = (char**)malloc(nm_line * sizeof(char*));

	for (int i = 0; i < nm_line; i++) {
		in_arr[i] = (char*)malloc(num1 * sizeof(char));
	}

	char* point = strtok(input_ch, "\n");
	int arr_num = 0;

	while (point != NULL) {
		strcpy(in_arr[arr_num], point);
		arr_num++;
		point = strtok(NULL, "\n");
	}

	for (int i = 0; i < nm_line; i++) {
		printf("Line %d %s\n", i + 1, in_arr[i]);

		char* point_r = strtok(in_arr[i], " ");
		if (strcmp(point_r, t[0].nm) == 0)
			continue;

		while (point_r != NULL) {
			if (strcmp(point_r, t[1].nm) == 0) {
				printf("%s\n", t[1].nm);
			}
			else if (strstr(point_r, ";") != NULL) {
				char* ptr3 = strtok(point_r, ";");
				printf("Id %s\n", ptr3);
				printf(";\n");
			}
			else if (strcmp(point_r, "0") == 0) {
				printf("%s %s\n;\n", t[5].num, point_r);
			}
			else if (strcmp(point_r, t[3].nm) == 0) {
				printf("%s\n", t[3].nm);
			}
			else if (strcmp(point_r, t[18].nm) == 0) {
				printf("%s\n", t[18].nm);
			}
			else if (strcmp(point_r, t[4].nm) == 0) {
				printf("%s\n", t[4].nm);
			}
			else if (strcmp(point_r, t[6].nm) == 0) {
				printf("%s\n", t[6].nm);
			}
			else if (strcmp(point_r, t[8].nm) == 0) {
				printf("%s\n", t[8].nm);
			}
			else if (strcmp(point_r, t[9].nm) == 0) {
				printf("%s\n", t[9].nm);
			}
			else if (strcmp(point_r, t[10].nm) == 0) {
				printf("%s\n", t[10].nm);
			}
			else if (strcmp(point_r, t[11].nm) == 0) {
				printf("%s\n", t[11].nm);
			}
			else if (strcmp(point_r, t[12].nm) == 0) {
				printf("%s\n", t[12].nm);
			}
			else if (strcmp(point_r, t[13].nm) == 0) {
				printf("%s\n", t[13].nm);
			}
			else if (strcmp(point_r, t[15].nm) == 0) {
				printf("%s\n", t[15].nm);
			}
			else if (strcmp(point_r, t[16].nm) == 0) {
				printf("%s\n", t[16].nm);
			}
			else if (strcmp(point_r, t[17].nm) == 0) {
				printf("%s\n", t[17].nm);
			}
			else if (strstr(point_r, ".") != NULL) {
				printf("%s %g\n", t[7].nm, atof(point_r, NULL));
			}
			else if (strcmp(point_r, ",") == 0) {
				printf("");
			}
			else if (atoi(point_r) != NULL) {
				printf("%s %d\n", t[5].nm, atoi(point_r));
			}
			else if (atoi(point_r) == 0) {
				printf("%s %d\n", t[5].nm, atoi(point_r));
			}
			else {
				printf("%s %s\n", t[2].nm, point_r);
			}
			if (strstr(point_r, t[3].nm) != NULL) {
				printf("%s\n", t[3].nm);
			}
			else if (strstr(point_r, t[14].nm) != NULL) {
				printf("%s\n", t[14].nm);
			}
			point_r = strtok(NULL, " ");
		}
	}


	for (int i = 0; i < nm_line; i++) {
		free(in_arr[i]);
	}
	free(in_arr);
	return 0;
}

int main()
{

	char input_str[] = { "// this is a comment to be ignored\nint a , b;\na245 = 53 + b + 42.345;\n// a-b+c+b\nif ( a245 <= ( 182957 ) Variable 25.12 == what1919$;\nfor ( i = 0; a < x; i++ ) {\n a = a * 2;" };
	int line = 1;
	get_Token(input_str);
	return 0;
}