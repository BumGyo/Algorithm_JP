package p201;

import java.util.ArrayList;
import java.util.HashSet;

//以下のimportの内容について理解できなくても課題を解くのに支障はありません
//テスト用
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/* 感想をこの下の行から記述する //注意：この行を改変すると感想がないと見なされます

次数に基づく枝向き付けと先行点の共通要素探索で、三角形列挙が効率化できる点を理解した。
難しくてネットで方法を調べた。

// 感想をこの上の行までに記述する */ //注意：この行を改変すると感想がないと見なされます


public class TriangleEnumeration{
	
	public static void main(String[] args){
		
		
		// 作成したメソッドのテストを行うメソッドです。
		// このメソッドがfalseを出力した場合、解答のメソッドは正しく設計されていません。
		// ただし、falseが出力されなかったとしても正解とは限りません。
		test();
		
	}
	
	/* αコメントをこの下の行から記述する //注意：この行を改変するとαコメントがないと見なされます
	
    無向グラフの隣接リストGを昇順で構築し、各頂点の次数degを得る。
	各無向枝{u,v}を、(deg[u] < deg[v])または(deg[u] == deg[v]かつu < v)のときu→vと向き付け、
	有向グラフの先行点リストinNeighborsを作る（重複検出を防ぐための Itai–Rodeh 型規則）。
	各頂点v1について、inNeighbors[v1]の各頂点v2とinNeighbors[v2]の交差を二本指法で取り、
	共通要素v3ごとに[v1, v2, v3]を三角形として収集する。
	この向き付けにより各三角形はちょうど一度だけ検出される。

	// αコメントをこの上の行までに記述する */ //注意：この行を改変するとαコメントがないと見なされます
	
	private static ArrayList<ArrayList<Integer>> enumerate(int[][] ar_edge, int nodenum){

		//* 無向グラフの隣接リストGを初期化
        //* nodenum 個の空リストを用意して頂点ごとの隣接先を格納
        ArrayList<ArrayList<Integer>> G = new ArrayList<>();
        
        for(int i = 0; i < nodenum; i++) {
        	G.add(new ArrayList<>());
        }
        
        //* 枝配列ar_edgeを走査し、無向辺u-vを双方のリストに追加
        //* 入力の境界を簡易に確認しつつ、G[u]とG[v]に互いを追加
        for(int i = 0; i < ar_edge.length; i++){
            int u = ar_edge[i][0];
            int v = ar_edge[i][1];
            if(u < 0 || v < 0 || u >= nodenum || v >= nodenum) continue;
            G.get(u).add(v);
            G.get(v).add(u);
        }
        
        //* 各頂点の隣接リストを昇順に整列し、次数配列degを計算
        //* Collections.sort によりG[v]をソートし、サイズをdeg[v]に記録
        int[] deg = new int[nodenum];
        for(int v = 0; v < nodenum; v++){
            Collections.sort(G.get(v));
            deg[v] = G.get(v).size();
        }

        //* Itai–Rodeh 型の規則で枝を向き付け、先行点リスト inNeighborsを作る
        //* 小次数→大次数（同数は小番号→大番号）へ向け、受け手頂点の inNeighborsに送り手を追加
        ArrayList<ArrayList<Integer>> inNeighbors = new ArrayList<>();
        for(int i = 0; i < nodenum; i++) {
        	inNeighbors.add(new ArrayList<>());
        }

        for(int u = 0; u < nodenum; u++){
            for(int v : G.get(u)){
                if(u < v){ 
                    boolean uToV = (deg[u] < deg[v]) || (deg[u] == deg[v] && u < v);
                    if(uToV){
                        inNeighbors.get(v).add(u);
                    } else {
                        
                        inNeighbors.get(u).add(v);
                    }
                }
            }
        }
        
        //* 先行点リストも昇順整列し、後の二本指交差を効率化
        //* 各vについてinNeighbors[v]をCollections.sortで整列
        for(int v = 0; v < nodenum; v++){
            Collections.sort(inNeighbors.get(v));
        }

        //* 三角形列挙：各v1に対しinNeighbors[v1]内のv2と、
        //* inNeighbors[v1], inNeighbors[v2]の交差（共通先行点v3を二本指で探索する
        //* a==bのとき[v1, v2, a]を三角形として収集し、ポインタを同時に進める
        ArrayList<ArrayList<Integer>> ans = new ArrayList<>();
        for(int v1 = 0; v1 < nodenum; v1++){
            ArrayList<Integer> pred1 = inNeighbors.get(v1);
            int sz1 = pred1.size();
            for(int i = 0; i < sz1; i++){
                int v2 = pred1.get(i);
                ArrayList<Integer> pred2 = inNeighbors.get(v2);
                
                int p1 = 0, p2 = 0;
                while(p1 < pred1.size() && p2 < pred2.size()){
                    int a = pred1.get(p1);
                    int b = pred2.get(p2);
                    if(a == v2) { 
                        p1++;
                        continue;
                    }
                    if(b == v1) { 
                        p2++;
                        continue;
                    }
                    if(a == b) {
                        ArrayList<Integer> tri = new ArrayList<>(Arrays.asList(v1, v2, a));
                        ans.add(tri);
                        p1++; p2++;
                    } else if(a < b) {
                        p1++;
                    } else {
                        p2++;
                    }
                }
            }
        }
        return ans;
	}
	
	// 作成したメソッドのテストを行うメソッドです。
	// このメソッドがfalseを出力した場合、解答のメソッドが正しく設計されていません。
	// ただし、falseが出力されなかったとしても正解とは限りません。
	private static void test(){
		
		//test1
		int nodenumtest1 = 4;
		int[][] ar_edgetest1 = {{0, 1}, {0, 2}, {1, 2}, {1, 3}};
		ArrayList<ArrayList<Integer>> list_tritest1ans1 = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> list_tri1 = new ArrayList<Integer>(Arrays.asList(0, 1, 2));
		list_tritest1ans1.add(list_tri1);
		//System.out.println(list_tritest1ans1);
		//test1-1
		boolean test1res1 = false;
		ArrayList<ArrayList<Integer>> list_tritest1res1 = enumerate(ar_edgetest1, nodenumtest1);
		//System.out.println(list_tritest1res1);
		for(ArrayList<Integer> list1: list_tritest1res1){
			Collections.sort(list1);
		}
		//System.out.println(list_tritest1res1);
		test1res1 = list_tritest1res1.equals(list_tritest1ans1);
		System.out.println(test1res1);
		
		//test2
		int nodenumtest2 = 5;
		int[][] ar_edgetest2 = {{3, 2}, {2, 1}, {2, 4}, {4, 3}, {0, 4}, {4, 1}, {0, 2}};
		int[][] ar_tritest2ans1 = {{0, 2, 4}, {1, 2, 4}, {2, 3, 4}};
		ArrayList<ArrayList<Integer>> list_tritest2ans1 = new ArrayList<ArrayList<Integer>>();
		for(int a1 = 0; a1 < ar_tritest2ans1.length; a1++){
			int node1 = ar_tritest2ans1[a1][0];
			int node2 = ar_tritest2ans1[a1][1];
			int node3 = ar_tritest2ans1[a1][2];
			list_tri1 = new ArrayList<Integer>(Arrays.asList(node1, node2, node3));
			list_tritest2ans1.add(list_tri1);
		}
		//System.out.println(list_tritest2ans1);
		//test2-1
		boolean test2res1 = false;
		ArrayList<ArrayList<Integer>> list_tritest2res1 = enumerate(ar_edgetest2, nodenumtest2);
		//System.out.println(list_tritest2res1);
		for(ArrayList<Integer> list1: list_tritest2res1){
			Collections.sort(list1);
		}
		Collections.sort(list_tritest2res1, new ForComp());
		//System.out.println(list_tritest2res1);
		test2res1 = list_tritest2res1.equals(list_tritest2ans1);
		System.out.println(test2res1);
		
		
	}
	
}

class ForComp implements Comparator<ArrayList<Integer>>{
	
	@Override
	public int compare(ArrayList<Integer> list1, ArrayList<Integer> list2) {
		for(int a1 = 0; a1 < 3; a1++){
			if(list1.get(a1) < list2.get(a1)){
				return -1;
			}
			else
			if(list1.get(a1) > list2.get(a1)){
				return 1;
			}
		}
		return -1;
	}
}
