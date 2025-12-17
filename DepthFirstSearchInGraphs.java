package m203;

import java.util.ArrayList;
//以下のimportの内容について理解できなくても課題を解くのに支障はありません
//テスト用
import java.util.Arrays;
import java.util.Stack;

/* 感想をこの下の行から記述する //注意：この行を改変すると感想がないと見なされます

スタックを用いた非再帰DFSでは、取り出し順と隣接リストの順序が訪問順に直接影響するため、pushの順を仕様に合わせて統一する重要性を理解した。
循環を含むグラフでは訪問済み集合により一回限りの訪問に抑える設計が鍵であり、取り出し時に未訪問判定を行うことで前順の一貫性が保てた。
テストケースとの突き合わせで、隣接リスト順での挿入規約を守ることが期待解の獲得に直結することを確認した。

// 感想をこの上の行までに記述する */ //注意：この行を改変すると感想がないと見なされます



public class DepthFirstSearchInGraphs{
	
	public static void main(String[] args){
		
		
		// 作成したメソッドのテストを行うメソッドです。
		// このメソッドがfalseを出力した場合、解答のメソッドは正しく設計されていません。
		// ただし、falseが出力されなかったとしても正解とは限りません。
		test();
		
	}
	
	
	/* αコメントをこの下の行から記述する //注意：この行を改変するとαコメントがないと見なされます
	
	このメソッドは、連結無向グラフの隣接リスト list_adjlist と始点 initnode を入力として深さ優先探索を実行し、
	前順の訪問順を格納した ArrayList<Integer> を出力する。
	探索は Stack を用いた非再帰方式で管理し、スタックから頂点 v を取り出した時点で未訪問なら訪問とみなし順序に追加し、
	v の隣接頂点を隣接リストに記載された順で push する。
	訪問済み集合により各頂点の重複探索を防ぎ、各頂点・各枝を高々一定回数だけ処理するため
	計算量は頂点数 n と枝数 m に対して O(n + m) となる。
	
	// αコメントをこの上の行までに記述する */ //注意：この行を改変するとαコメントがないと見なされます
	private static ArrayList<Integer> search(ArrayList<ArrayList<Integer>> list_adjlist, int initnode){
		
		//* 出力用の前順訪問リストを初期化する
		ArrayList<Integer> preorder = new ArrayList<>();
		
		//* グラフの頂点数を取得し、空グラフであれば即時に空リストを返す
		int n = list_adjlist.size();
		if (n == 0) return preorder;
		
		//* 訪問済み管理用のブール配列を用意
		boolean[] visited = new boolean[n];
		
		//* 非再帰DFSのためのスタックを用意し、始点を投入して探索を開始する
		Stack<Integer> stack = new Stack<>();
		stack.push(initnode);
		
		//* スタックが空になるまで探索を継続し、深さ優先で頂点を処理する
		while(!stack.isEmpty()) {
			
			//* 次に処理すべき頂点を取り出す（LIFO）
			int v = stack.pop();
			
			//* 既に訪問済みの頂点はスキップ
			if(visited[v]) continue;
			
			//* v を訪問済みに設定し、前順リストに追加して訪問順を記録する
			visited[v] = true;
			preorder.add(v);
			
			//* v の隣接頂点列を取得し、未訪問のものだけを push する
		    //* 隣接リストの順序で push することで、期待される前順を規定する
			ArrayList<Integer> neighbors = list_adjlist.get(v);
			for(int u : neighbors) {
				if(!visited[u]) {
					stack.push(u);
				}
			}
		}
		
		//* 全探索完了後、前順の訪問順リストを返す
		return preorder;
	}
	
	
	// 作成したメソッドのテストを行うメソッドです。
	// このメソッドがfalseを出力した場合、解答のメソッドが正しく設計されていません。
	// ただし、falseが出力されなかったとしても正解とは限りません。
	private static void test(){
		//test1
		ArrayList<ArrayList<Integer>> list_adjlisttest1 = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> list1 = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
		list_adjlisttest1.add(list1);
		list1 = new ArrayList<Integer>(Arrays.asList(0, 2, 6));
		list_adjlisttest1.add(list1);
		list1 = new ArrayList<Integer>(Arrays.asList(0, 1, 5));
		list_adjlisttest1.add(list1);
		list1 = new ArrayList<Integer>(Arrays.asList(0, 5));
		list_adjlisttest1.add(list1);
		list1 = new ArrayList<Integer>(Arrays.asList(0, 6, 7, 8));
		list_adjlisttest1.add(list1);
		list1 = new ArrayList<Integer>(Arrays.asList(2, 3));
		list_adjlisttest1.add(list1);
		list1 = new ArrayList<Integer>(Arrays.asList(1, 4, 7));
		list_adjlisttest1.add(list1);
		list1 = new ArrayList<Integer>(Arrays.asList(4, 6));
		list_adjlisttest1.add(list1);
		list1 = new ArrayList<Integer>(Arrays.asList(4));
		list_adjlisttest1.add(list1);
		//System.out.println(list_adjlisttest1);//[[1, 2, 3, 4], [0, 2, 6], [0, 1, 5], [0, 5], [0, 6, 7, 8], [2, 3], [1, 4, 7], [4, 6], [4]]
		//test1-1
		ArrayList<Integer> list_test1ans1 = new ArrayList<Integer>(Arrays.asList(0, 4, 8, 7, 6, 1, 2, 5, 3));
		boolean test1res1 = false;
		ArrayList<Integer> list_test1res1 = search(list_adjlisttest1, 0);
		//System.out.println(list_test1res1 + " " + list_test1ans1);
		test1res1 = list_test1res1.equals(list_test1ans1);
		//test1-2
		ArrayList<Integer> list_test1ans2 = new ArrayList<Integer>(Arrays.asList(8, 4, 7, 6, 1, 2, 5, 3, 0));
		boolean test1res2 = false;
		ArrayList<Integer> list_test1res2 = search(list_adjlisttest1, 8);
		//System.out.println(list_test1res2 + " " + list_test1ans2);
		test1res2 = list_test1res2.equals(list_test1ans2);
		System.out.println(test1res1 + " " + test1res2);
		
	}
	
}

