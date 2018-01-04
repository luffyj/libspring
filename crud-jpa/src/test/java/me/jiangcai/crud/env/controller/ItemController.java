package me.jiangcai.crud.env.controller;

import me.jiangcai.crud.controller.AbstractCrudController;
import me.jiangcai.crud.env.entity.Item;
import me.jiangcai.crud.row.FieldDefinition;
import me.jiangcai.crud.row.field.Fields;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author CJ
 */
@Controller
@RequestMapping("/items")
public class ItemController extends AbstractCrudController<Item, Long> {
    @Override
    protected Object describeEntity(Item origin) {
        return super.describeEntity(origin);
    }

    @Override
    protected List<FieldDefinition<Item>> listFields() {
        return Arrays.asList(
                Fields.asBasic("id"),
                Fields.asBasic("name"),
                Fields.asBasic("amount")
        );
    }

    @Override
    protected Specification<Item> listSpecification(Map<String, Object> queryData) {
        return null;
    }

    @Override
    protected void preparePersist(Item data, Map<String, Object> otherData) {
        super.preparePersist(data, otherData);
    }
}
