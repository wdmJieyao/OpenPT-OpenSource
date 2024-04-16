package fun.hzaw.commonbean.collection;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

public class CommListUtils {
    private CommListUtils() {
    }


    /**
     * 增强校验LIST集合是否为空
     */
    public static boolean ListIsEmptyEx(List<?> targList) {
        if (CollectionUtils.isEmpty(targList)) {
            return true;
        }

        return targList.stream()
                .allMatch(curr -> {
                    // 字符串
                    if (curr instanceof String strParam) {
                        return StringUtils.isBlank(strParam);
                    }
                    // List
                    if (curr instanceof List<?> listParam) {
                        return ListIsEmptyEx(listParam);
                    }

                    // todo Map为空的情况
                    return Objects.isNull(curr);
                });

    }


    // public static void main(String[] args) {
    //     System.out.println(ListIsEmptyEx(Lists.newArrayList("", Lists.newArrayList("", "", "", ""), "")));
    // }
}
