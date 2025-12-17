package p211;

import java.util.ArrayList;
import java.util.HashMap;

/* 感想をこの下の行から記述する //注意：この行を改変すると感想がないと見なされます

ハフマン符号では頻度の高い文字ほど短いビット列を割り当てることで、全体として平均符号長を最小化できるというアイデアが非常に直感的だっと感じた。  
実装では、最小ヒープを使って頻度の小さい2つの節点を繰り返し取り出し、
新しい親ノードとして結合していくことで、最適な接頭符号木を貪欲的に構成できることを確認した。  
また、符号木の葉ノードに対応する文字を HashMap で管理し、
根から葉への経路を 0/1 でたどることで符号語を生成する処理は、木構造とビット列の対応関係を理解するうえで分かりやすかった。  


// 感想をこの上の行までに記述する */ //注意：この行を改変すると感想がないと見なされます


class BinaryTreeNode{
	int value; // 頂点に格納される値
	BinaryTreeNode left; // 左の子
	BinaryTreeNode right; // 右の子
	
	public BinaryTreeNode(){
		value = -1;
		left = null;
		right = null;
	}
	
	public BinaryTreeNode(int val1, BinaryTreeNode left1, BinaryTreeNode right1){
		value = val1;
		left = left1;
		right = right1;
	}
}

public class HuffmanCode{
	public static void main(String[] args){
		// 作成したメソッドのテストを行うメソッドです。
		// このメソッドがfalseを出力した場合、解答のメソッドは正しく設計されていません。
		// ただし、falseが出力されなかったとしても正解とは限りません。
		test();
	}
	
	
	
	/* αコメントをこの下の行から記述する //注意：この行を改変するとαコメントがないと見なされます
	
	encode は各文字の出現頻度を格納した配列 ar_freq を受け取り、ハフマン符号木を表す BinaryTreeNode 配列を構成して返す。  
	長さ n の頻度配列からは葉ノード n 個と内部ノード n−1 個、合計 2n−1 個の節点が生成されるため、
	あらかじめその大きさの配列を確保し、先頭 n 個に葉ノード、残りに内部ノードを格納する。  
	最小ヒープ MyHeap には「配列上のインデックス」と「対応する頻度（部分木の重み）」のペアを登録し、
	頻度の小さい2つの節点を繰り返し取り出して親ノードにマージすることで、重みの小さい部分木から順に接続していく。  
	
	// αコメントをこの上の行までに記述する */ //注意：この行を改変するとαコメントがないと見なされます
	private static BinaryTreeNode[] encode(int[] ar_freq){
		
		//*  文字種の数 nChars と、ハフマン木全体の節点数 nodeCount=2n−1 を求める
		int nChars = ar_freq.length;
	    int nodeCount = 2 * nChars - 1;

	    //* すべての節点を格納する配列 tree を確保する
	    BinaryTreeNode[] tree = new BinaryTreeNode[nodeCount];
	    
	    //* 頻度の小さい節点を効率よく取り出すための最小ヒープ MyHeap を初期化する
	    MyHeap pq = new MyHeap();

	    //* 頻度配列の各要素について葉ノードを生成し、配列先頭に格納すると同時にヒープへ登録する
	    for (int i = 0; i < nChars; i++) {
	        tree[i] = new BinaryTreeNode(ar_freq[i], null, null);
	        pq.insert(i, ar_freq[i]);
	    }

	    //* 次に追加される内部ノードの格納位置を示すインデックス curIndex を葉ノードの直後から開始
	    int curIndex = nChars;

	    //* ヒープに 2 個以上の節点が残っている間、頻度最小の 2 つを取り出して親ノードに統合する
	    while (pq.size() > 1) {
	        
	    	//* 最小頻度の節点 top1 と次点 top2 を取り出し、それぞれのインデックスと頻度を取得
	    	ArrayList<Integer> top1 = pq.extractMin();
	        ArrayList<Integer> top2 = pq.extractMin();
	        int idx1 = top1.get(0);
	        int freq1 = top1.get(1);
	        int idx2 = top2.get(0);
	        int freq2 = top2.get(1);

	        //* 2 つの部分木の重みを合計した mergedFreq を親ノードの value として用いる
	        int mergedFreq = freq1 + freq2;
	        
	        //* 親ノードの左・右の子として、それぞれの部分木（tree[idx1], tree[idx2]）を接続する
	        BinaryTreeNode leftNode = tree[idx1];
	        BinaryTreeNode rightNode = tree[idx2];
	        tree[curIndex] = new BinaryTreeNode(mergedFreq, leftNode, rightNode);
	        
	        //* 新しく作成した内部ノードも、部分木として次のマージ対象になるようヒープに再登録する
	        pq.insert(curIndex, mergedFreq);

	        //* 次に生成される内部ノードのために格納位置インデックスを 1 つ進める
	        curIndex++;
	    }

	    //* すべてのマージが完了したら返す
	    return tree;
	}
	
	
	// 符号木を根から前順でなぞって符号語を取得するメソッド
	private static void preordertraverse(HashMap<String, String> hmap_code, BinaryTreeNode ctreenode, ArrayList<String> list_code, HashMap<BinaryTreeNode, String> hmap_ctreenode2char){
		if(ctreenode == null){
			return;
		}
		// 文字を対応付けた頂点かどうか判定する
		if(hmap_ctreenode2char.containsKey(ctreenode) == true){
			// 文字を対応付けた頂点だった場合、根からのなぞりの結果(list_code)から符号語を求める
			String str_word = "";
			for(String str1: list_code){
				str_word += str1;
			}
			// 文字と符号語の対応関係を記録する
			hmap_code.put(hmap_ctreenode2char.get(ctreenode), str_word);
		}
 		// 左の子を根とする部分木に対して再帰する
		// 左の子への枝は0が対応付けられる
		list_code.add("0");
		preordertraverse(hmap_code, ctreenode.left, list_code, hmap_ctreenode2char);
		// 再帰が終了したので、左の子への枝に対応付けられた0を削除する
		list_code.remove(list_code.size()-1);
		// 右の子を根とする部分木に対して再帰する
		// 右の子への枝は1が対応付けられる
		list_code.add("1");
		preordertraverse(hmap_code, ctreenode.right, list_code, hmap_ctreenode2char);
		// 再帰が終了したので、左の子への枝に対応付けられた1を削除する
		list_code.remove(list_code.size()-1);
	}
	
	// 符号木から符号語を取得するメソッド
	private static HashMap<String, String> getCode(String[] ar_char, BinaryTreeNode[] ar_ctree){
		// 文字の数
		int charnum = ar_char.length;
		// 符号木の頂点をキー、文字を値とするHashMap（頂点のなぞりにおいて、頂点と文字の対応関係を調べるのに使用する）
		HashMap<BinaryTreeNode, String> hmap_ctreenode2char = new HashMap<>();
		for(int a1 = 0; a1 < charnum; a1++){
			hmap_ctreenode2char.put(ar_ctree[a1], ar_char[a1]);
		}
		// 文字をキー、符号語を値とするHashMap
		HashMap<String, String> hmap_code = new HashMap<String, String>();
		// 符号木を前順でなぞって符号語を取得する
		preordertraverse(hmap_code, ar_ctree[ar_ctree.length-1], new ArrayList<String>(), hmap_ctreenode2char);
		// 符号を格納したHashMapを返す
		return hmap_code;
	}
	
	// 作成したメソッドのテストを行うメソッドです。
	// このメソッドがfalseを出力した場合、解答のメソッドが正しく設計されていません。
	// ただし、falseが出力されなかったとしても正解とは限りません。
	private static void test(){
		
		
		//test1
		int[] ar_freqtest1 = {45, 13, 12, 16, 9, 5};
		String[] ar_chartest1 = {"a", "b", "c", "d", "e", "f"};
		//test1-1
		HashMap<String, String> hmap_codetest1ans1 = new HashMap<String, String>()
		{
			{
				put("a", "0");
				put("b", "101");
				put("c", "100");
				put("d", "111");
				put("e", "1101");
				put("f", "1100");
			}
		};
		boolean test1res1 = false;
		BinaryTreeNode[] ar_ctree1ans1 = encode(ar_freqtest1);
		HashMap<String, String> hmap_codetest1res1 = getCode(ar_chartest1, ar_ctree1ans1);
		//System.out.println(hmap_codetest1res1);
		test1res1 = hmap_codetest1res1.equals(hmap_codetest1ans1);
		System.out.println(test1res1);
		
		//test2 //キング牧師の演説``I hava a dream''より
		int[] ar_freqtest2 = {550, 511, 177, 363, 94, 124, 647, 568, 21, 459, 148, 168, 251, 323, 162, 408, 407, 854, 224, 78, 164, 104, 40, 5, 4, 6};
		String[] ar_chartest2 = {"i", "a", "m", "h", "p", "y", "t", "o", "j", "n", "w", "u", "d", "l", "g", "s", "r", "e", "f", "v", "c", "b", "k", "x", "z", "q"};
		//test2-1
		HashMap<String, String> hmap_codetest2ans1 = new HashMap<String, String>()
		{
			{
				put("a", "1100");
				put("b", "010111");
				put("c", "00110");
				put("d", "10111");
				put("e", "100");
				put("f", "10110");
				put("g", "111111");
				put("h", "0100");
				put("i", "1101");
				put("j", "111110001");
				put("k", "11111001");
				put("l", "0010");
				put("m", "01010");
				put("n", "1010");
				put("o", "1110");
				put("p", "010110");
				put("q", "1111100000");
				put("r", "0110");
				put("s", "0111");
				put("t", "000");
				put("u", "00111");
				put("v", "1111101");
				put("w", "111101");
				put("x", "11111000011");
				put("y", "111100");
				put("z", "11111000010");
			}
		};
		boolean test2res1 = false;
		BinaryTreeNode[] ar_ctree2ans1 = encode(ar_freqtest2);
		HashMap<String, String> hmap_codetest2res1 = getCode(ar_chartest2, ar_ctree2ans1);
		//System.out.println(hmap_codetest2res1);
		test2res1 = hmap_codetest2res1.equals(hmap_codetest2ans1);
		System.out.println(test2res1);
		
	}
}

class MyHeap{
	// ヒープ本体
	static ArrayList<ArrayList<Integer>> list_heap;
	// ヒープのキー（ヒープ内の頂点の格納位置（インデックス））の管理
	static HashMap<Integer, Integer> hmap_key2index;
	
	public MyHeap(){
		// 初期化
		list_heap = new ArrayList<ArrayList<Integer>>();
		hmap_key2index = new HashMap<Integer, Integer>();
	}
	
	// インデックスindex1の頂点の（より小さい数値をもつ）子供のインデックスを取得する
	private static int getChildIndex(int index1){
		//左の子供のインデックスを取得する
		int leftchildindex = getLeftChildIndex(index1);
		// ヒープの大きさ
		int length = list_heap.size();
		// 左の子供が存在しない（＝子供が存在しない）場合
		if(length <= leftchildindex){
			return -1;
		}
		// 右の子供のインデックスを取得する
		int rightchildindex = getRightChildIndex(index1);
		// 左の子供のみ存在する場合
		if(length <= rightchildindex){
			//左の子供のインデックスを返す
			return leftchildindex;
		}
		// 左と右の子供のうち小さい数値を格納している子供のインデックスを返す
		if(compare(list_heap.get(leftchildindex).get(1), list_heap.get(rightchildindex).get(1)) == true){
			return leftchildindex;
		}
		return rightchildindex;
	}
	
	// ヒープに格納されている要素（データ）の数を取得する
	public int size(){
		return list_heap.size();
	}
	
	// ヒープに格納されている要素（データ）を出力する
	public void output(){
		System.out.println(list_heap); 
	}
	
	// 2つの子供・親子に格納される数値の大小関係を調べる
	private static boolean compare(int val1, int val2){
		return (val1 <= val2);
	}
	
	//インデックスindex1の頂点の親のインデックスを取得する
	private static int getParentIndex(int index1){
		if(index1 <= 0){
			return -1;
		}
		return (int)((index1-1) / 2);
	}
	
	
	//インデックスindex1の頂点の左の子のインデックスを取得する
	private static int getLeftChildIndex(int index1){
		return 2 * index1 + 1;
	}
	
	//インデックスindex1の頂点の右の子のインデックスを取得する
	private static int getRightChildIndex(int index1){
		return 2 * index1 + 2;
	}
	
	// 最小値を持つデータの取り出し
	public ArrayList<Integer> extractMin(){
		// ヒープが空の場合
		if(size() == 0){
			return null;
		}
		
		// 先頭（根）のデータ（＝このデータの数値が最小値）を取り出す（返り値）
		ArrayList<Integer> list_rootdata = list_heap.get(0);
		// ヒープ内の頂点の格納位置を削除
		hmap_key2index.remove(list_rootdata.get(0));
		// 最後尾の要素（データ）を先頭に移す
		ArrayList<Integer> tail1 = list_heap.get(list_heap.size()-1);
		list_heap.set(0, tail1);
		// 最後尾の要素（データ）を削除する
		list_heap.remove(list_heap.size()-1);
		// 先頭の値を下移動する
		movedownward(0);
		// 最小値を持つデータを返す
		return list_rootdata;
	}
	
	// 挿入
	public void insert(int key1, int value1){
		// ヒープに挿入するデータ（キー、値）作成
		ArrayList<Integer> list_data = new ArrayList<Integer>();
		list_data.add(key1);
		list_data.add(value1);
		// まだ登録されていないキーの場合
		if(hmap_key2index.containsKey(key1) == false){
			// 最後尾にデータを挿入する
			list_heap.add(list_data);
			// 挿入した最後尾のデータを上移動する
			int index1 = list_heap.size();
			moveupward(index1-1);
			return;
		}
		
		// 既に登録されているキーの場合
		// 当該のキーの現在のヒープ内の位置（インデックス）を取得する
		int valueindex = hmap_key2index.get(key1);
		//
		ArrayList<Integer> list_currentdata = list_heap.get(valueindex);
		// 当該のキーの現在の数値との大小を比較して更新する必要があるかどうか調べる
		// 更新不要の場合
		if(compare(list_currentdata.get(1), list_data.get(1)) == true){
			return;
		}
		// 更新が必要となる場合
		// 要素（データ）更新
		list_heap.set(valueindex, list_data);
		// 上移動させる
		moveupward(valueindex);
	}
	
	// インデックスindex1の位置の要素（データ）を下移動する
	private static void movedownward(int index1){
		// 要素（データ）が存在しない場合
		if(list_heap.size() == 0){
			return;
		}
		// 上移動させる頂点（インデックスindex1に格納されている頂点）に格納されているデータを取得する
		ArrayList<Integer> list_data = list_heap.get(index1);
		// 下移動させる頂点のデータの数値
		int val1 = list_data.get(1);
		// 下移動させる頂点のデータのキー
		int key1 = list_data.get(0);
		//インデックスindex1の頂点の子供（ただし、2つ子供がある場合は、より小さい数値を格納している子供）のインデックスを取得する
		int childindex = getChildIndex(index1);
		
		// 子供がいなくなるまで頂点を下に移動させる（下移動）
		while(childindex != -1){
			// 下に移動させている頂点に格納されているデータの数値とその子供に格納されているデータの数値を比較し、
			// 下に移動が必要ない場合は下移動を終了する
			if(compare(val1, list_heap.get(childindex).get(1)) == true){
				break;
			}
			// index1の頂点に子供のデータを格納する（ヒープの要素の更新）
			update(index1, list_heap.get(childindex));
			
			// 次に下に移動させる頂点のインデックス（childindex）を代入する
			index1 = childindex;
			// 次に下に移動させる頂点の子供（ただし、2つ子供がある場合は、より小さいデータの数値を格納している子供）のインデックスを取得する
			childindex = getChildIndex(index1);
		}
		// 下移動完了時に、移動が完了した頂点に移動させた頂点のデータ（メソッド呼び出し時のindex1のデータ）を代入する
		update(index1, list_data);
		//
		return;
	}
	
	// インデックスindex1の位置の要素（データ）を上移動する
	private static void moveupward(int index1){
		// 要素（データ）が存在しない場合
		if(list_heap.size() == 0){
			return;
		}
		// 上移動させる頂点（インデックスindex1に格納されている頂点）に格納されているデータを取得する
		ArrayList<Integer> list_data = list_heap.get(index1);
		// 上移動させる頂点のデータの数値
		int val1 = list_data.get(1);
		// 上移動させる頂点のデータのキー
		int key1 = list_data.get(0);
		//インデックスindex1の頂点の親のインデックスを取得する
		int parentindex = getParentIndex(index1);
		
		// 親がいなくなるまで頂点を上に移動させる（上移動）
		while(parentindex != -1){
			// 上に移動させている頂点に格納されているデータの数値とその親に格納されているデータの数値を比較し、
			// 上に移動が必要ない場合は上移動を終了する
			if(compare(list_heap.get(parentindex).get(1), val1) == true){
				 break;
			}
			// index1の頂点に親のデータを格納する（ヒープの要素の更新）
			update(index1, list_heap.get(parentindex));
			
			// 次に上に移動させる頂点のインデックス（parentindex）を代入する
			index1 = parentindex;
			// 次に上に移動させる頂点の親のインデックスを取得する
			parentindex = getParentIndex(index1);
		}
		// 上移動完了時に、移動が完了した頂点に移動させた頂点のデータ（メソッド呼び出し時のindex1のデータ）を代入する
		update(index1, list_data);
		//
		return;
	}
	
	// ヒープのインデックスindex1の位置の要素（データ）を更新する
	private static void update(int index1, ArrayList<Integer> list_data1){
		// ヒープの要素を更新する
		list_heap.set(index1, list_data1);
		// ヒープ内のキーの格納位置を更新する
		hmap_key2index.put(list_data1.get(0), index1);
	}
	
}
