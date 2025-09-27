package m201;

import java.util.ArrayList;
//以下のimportの内容について理解できなくても課題を解くのに支障はありません
//テスト用
import java.util.Arrays;
import java.util.Collections;

/* 感想をこの下の行から記述する //注意：この行を改変すると感想がないと見なされます

本日学んだ隣接リストに関する内容を実習で扱うのかと思い、不安を感じたため、授業の資料であるPPTをもう一度読み返した。
留学生であることもあり、この短い時間で授業内容を理解するのは難しく、AIを利用して翻訳しながら内容をできるだけ早く把握した。
一般的に探索や整列を行う場合は配列を使用し、追加や削除が多い場合にはこの隣接リストを用いる。

// 感想をこの上の行までに記述する */ //注意：この行を改変すると感想がないと見なされます


public class AdjacencyList{
	
	public static void main(String[] args){
		
		// 作成したメソッドのテストを行うメソッドです。
		// このメソッドがfalseを出力した場合、解答のメソッドは正しく設計されていません。
		// ただし、falseが出力されなかったとしても正解とは限りません。
		test();
		
	}
	
	/* αコメントをこの下の行から記述する //注意：この行を改変するとαコメントがないと見なされます
	
	Javaを学んだのはかなり前であり、new演算子を使ってオブジェクトを生成することに慣れていなかったため、
	ネットで調べてそしてヒントをみてこの部分を解決した。
	作成したメソッドは、辺の配列と頂点数を引数に受け取り、最終的にArrayListを返すものである。
	全ての頂点に対して空の隣接リストを生成し、各辺の情報を双方向に追加することで無向グラフを正確に表現している。
	さらに、各頂点のリスト内にある隣接頂点は昇順に整列して保存し、
	入力頂点番号は0以上nodenum-1以下であり、辺{u, v}の逆順{v, u}は含まれない前提でforループを構成した。
	以前に韓国でアルゴリズムを学んだ経験はあるものの、日本語の資料を理解するまでに時間を要し、難しさを感じた。
	
	// αコメントをこの上の行までに記述する */ //注意：この行を改変するとαコメントがないと見なされます
	
	//* int[][] ar_edge
	//* int nodenum = nodeの数
	private static ArrayList<ArrayList<Integer>> construct(int[][] ar_edge, int nodenum){
		//* 結果用の隣接リストを初期化し、全頂点に対応する空リストを用意
		ArrayList<ArrayList<Integer>> adjList = new ArrayList<>();
		
		//* 頂点0〜nodenum-1それぞれに対応する空のArrayList<Integer>を追加
		for (int i = 0; i < nodenum; i++) {
			adjList.add(new ArrayList<Integer>());
		}
		
		//* 入力の無向辺一覧を走査し、各辺(u,v)を両端点の隣接リストへ相互に追加
		for (int i = 0; i < ar_edge.length; i++) {
			//* i番目の辺の端点u,vを取り出す
			int u = ar_edge[i][0];
			int v = ar_edge[i][1];
			
			//* 無向性を反映するためuの隣接にvを、vの隣接にuを追加
			adjList.get(u).add(v);
			adjList.get(v).add(u);
		}
		
		//* 各頂点の隣接リストを昇順に整列して格納形式を規格化する
		for (int i = 0; i < nodenum; i++) {
			//* Collections.sortを用いてadjList[i]を昇順ソートする
			Collections.sort(adjList.get(i));
		}
		
		//* 構築が完了した隣接リスト全体を返却する
		return adjList;
	}
	
	
	// 作成したメソッドのテストを行うメソッドです。
	// このメソッドがfalseを出力した場合、解答のメソッドが正しく設計されていません。
	// ただし、falseが出力されなかったとしても正解とは限りません。
	private static void test(){
		
		//test1
		int nodenumtest1 = 4;
		int[][] ar_edgetest1 = {{0, 1}, {0, 2}, {1, 2}, {1, 3}};
		ArrayList<ArrayList<Integer>> list_adjlisttest1ans1 = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> list_adj1 = new ArrayList<Integer>(Arrays.asList(1, 2));
		list_adjlisttest1ans1.add(list_adj1);
		list_adj1 = new ArrayList<Integer>(Arrays.asList(0, 2, 3));
		list_adjlisttest1ans1.add(list_adj1);
		list_adj1 = new ArrayList<Integer>(Arrays.asList(0, 1));
		list_adjlisttest1ans1.add(list_adj1);
		list_adj1 = new ArrayList<Integer>(Arrays.asList(1));
		list_adjlisttest1ans1.add(list_adj1);
		//System.out.println(list_adjlisttest1ans1);
		//test1-1
		boolean test1res1 = false;
		ArrayList<ArrayList<Integer>> list_adjlisttest1res1 = construct(ar_edgetest1, nodenumtest1);
		//System.out.println(list_adjlisttest1res1);
		test1res1 = list_adjlisttest1res1.equals(list_adjlisttest1ans1);
		System.out.println(test1res1);
		
		//test2
		int nodenumtest2 = 5;
		int[][] ar_edgetest2 = {{3, 2}, {2, 1}, {2, 4}, {4, 3}, {0, 4}, {4, 1}, {0, 2}};
		ArrayList<ArrayList<Integer>> list_adjlisttest2ans1 = new ArrayList<ArrayList<Integer>>();
		list_adj1 = new ArrayList<Integer>(Arrays.asList(2, 4));
		list_adjlisttest2ans1.add(list_adj1);
		list_adj1 = new ArrayList<Integer>(Arrays.asList(2, 4));
		list_adjlisttest2ans1.add(list_adj1);
		list_adj1 = new ArrayList<Integer>(Arrays.asList(0, 1, 3, 4));
		list_adjlisttest2ans1.add(list_adj1);
		list_adj1 = new ArrayList<Integer>(Arrays.asList(2, 4));
		list_adjlisttest2ans1.add(list_adj1);
		list_adj1 = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3));
		list_adjlisttest2ans1.add(list_adj1);
		//System.out.println(list_adjlisttest2ans1);
		//test2-1
		boolean test2res1 = false;
		ArrayList<ArrayList<Integer>> list_adjlisttest2res1 = construct(ar_edgetest2, nodenumtest2);
		//System.out.println(list_adjlisttest2res1);
		test2res1 = list_adjlisttest2res1.equals(list_adjlisttest2ans1);
		System.out.println(test2res1);
		
	}
	
}
