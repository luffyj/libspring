package me.jiangcai.poi.template;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.jiangcai.lib.test.SpringWebTest;
import me.jiangcai.poi.template.service.POITemplateServiceImpl;
import me.jiangcai.poi.template.test.model.ModelOne;
import me.jiangcai.poi.template.test.model.ModelOneInOne;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.StreamUtils;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author CJ
 */
@ContextConfiguration(classes = POITemplateConfig.class)
public class POITemplateServiceTest extends SpringWebTest {

    @Autowired
    private POITemplateService poiTemplateService;

    @Test
    public void lcm() {
        System.out.println(POITemplateServiceImpl.lcm(1, 1));
        System.out.println(POITemplateServiceImpl.lcm(1, 2));
        System.out.println(POITemplateServiceImpl.lcm(2, 4));
        System.out.println(POITemplateServiceImpl.lcm(3, 6));
        System.out.println(POITemplateServiceImpl.lcm(3, 5));
    }

    @Test
    public void export() throws Exception {
//        exportOne("demo1");
        exportOne("demo2");
    }

    @Test
    public void bean() throws IOException, IllegalTemplateException {
        ArrayList<ModelOne> list = new ArrayList<>();
        int count = 4 + random.nextInt(3);
        while (count-- > 0)
            list.add(randomModelOne());
        openExecl("demo1bean", list);
    }

    private ModelOne randomModelOne() {
        ModelOne one = new ModelOne();
        one.setAddress(RandomStringUtils.randomAlphabetic(25));
        one.setMobile(randomMobile());
        one.setName(RandomStringUtils.randomAlphabetic(4));
        one.setOrderDate(LocalDateTime.now());
        one.setOrderId(RandomStringUtils.randomAlphanumeric(8));
        one.setPayMethod(RandomStringUtils.randomAlphabetic(6));
        int count = 2 + random.nextInt(3);
        one.setGoods(new ArrayList<>(count));
        while (count-- > 0)
            one.getGoods().add(randomModelOneInOne());
        return one;
    }

    private ModelOneInOne randomModelOneInOne() {
        ModelOneInOne one = new ModelOneInOne();
        one.setName(RandomStringUtils.randomAlphabetic(10));
        one.setAmount(1 + random.nextInt(10));
        one.setPrice(BigDecimal.valueOf(Math.abs(random.nextDouble())).movePointRight(2));
        return one;
    }

    private void exportOne(String name) throws IOException, IllegalTemplateException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(JsonParser.Feature.ALLOW_COMMENTS);

        JsonNode list = objectMapper.readTree(new ClassPathResource("/" + name + ".json").getFile());
        ArrayList<JsonNode> jsonNodeArrayList = new ArrayList<>();
        list.forEach(jsonNodeArrayList::add);
        openExecl(name, jsonNodeArrayList);
    }

    private void openExecl(String name, List<?> list) throws IOException, IllegalTemplateException {
        File targetFile = new File("target/" + name + ".xls");
        try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
            poiTemplateService.export(outputStream, pageable -> {
                if (pageable.getPageNumber() == 0)
                    return new PageImpl<>(list,pageable,list.size());
                return null;
            }, null, new ClassPathResource("/" + name + ".xml"), null);

            outputStream.flush();
        }

        String result = StreamUtils.copyToString(new FileInputStream(targetFile), Charset.forName("UTF-8"));
        System.out.println(result);

        if (Desktop.isDesktopSupported())
            Desktop.getDesktop().open(targetFile);
    }

}