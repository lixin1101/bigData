import org.lionsoul.jcseg.tokenizer.ASegment;
import org.lionsoul.jcseg.tokenizer.core.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;


public class AnaylyzerTools {

    public static ArrayList<String> anaylyzerWords(String str) throws Exception {

        JcsegTaskConfig config = new JcsegTaskConfig(AnaylyzerTools.class.getResource("").getPath() + "jcseg.properties");
        ADictionary dic = DictionaryFactory.createDefaultDictionary(config);
        ArrayList<String> list = new ArrayList<String>();
        ASegment seg = null;
        try {
            seg = (ASegment) SegmentFactory.createJcseg(JcsegTaskConfig.COMPLEX_MODE, new Object[]{config, dic});
        } catch (JcsegException e1) {
            e1.printStackTrace();
        }
        try {
            seg.reset(new StringReader(str));
            IWord word = null;
            while ((word = seg.next()) != null) {
                list.add(word.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) throws Exception {
        String str2 = "HBase中通过row和columns确定的为一个存贮单元称为cell。显示每个元素，每个 cell都保存着同一份数据的多个版本。版本通过时间戳来索引。迎泽区是繁华的地方,营业厅营业";

        String str = "中华人民共和国";
        List<String> list = AnaylyzerTools.anaylyzerWords(str);
        for (String word : list) {
            System.out.println(word);
        }
        System.out.println(list.size());
    }

}
