package com.xia.digun.passmetacore.model;

import com.xia.digun.passmetacore.Main;
import com.xia.digun.passmetacore.annotation.AggregateRoot;
import com.xia.digun.passmetacore.constant.SysConstant;
import com.xia.digun.passmetacore.constant.exception.MetaException;
import com.xia.digun.passmetacore.utils.XmlParseUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;

import java.net.URL;
import java.util.List;

/**
 * description: 抽象模型
 *
 * @author wanghaoxin
 * date     2022/8/12 16:49
 * @version 1.0
 */
@NoArgsConstructor
@Data
@AggregateRoot
public class DomainModel {
    private String version;
    private List<Model> models;

    public DomainModel getDomainModel() {
        try {
            URL url = Main.class.getResource(SysConstant.META_MODEL_ROOT_PATH);
            final Document document = XmlParseUtils.getDocument(url);
            final List<Model> nodes = XmlParseUtils.getNodes(document, "/domain-model/models/model", Model.class);
            final String version = XmlParseUtils.getNodeText(document, "/domain-model/version");
            this.setModels(nodes);
            this.setVersion(version);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public List<AbstractModel> listModelById(String modelId) {
        if (this.getModels() == null) {
            this.getDomainModel();
        }
        final String resourcePath = this.getModels().stream().filter(model -> StringUtils.endsWith(modelId, model.getId()))
                .map(Model::getResourcePath).findFirst().orElse(null);
        if (StringUtils.isEmpty(resourcePath)) {
            throw new MetaException();
        }
        return listByResourcePath(resourcePath);
    }

    public List<AbstractModel> listByResourcePath(String resourcePath) {
        List<AbstractModel> models;
        try {
            URL url = Main.class.getResource(resourcePath);
            final Document document = XmlParseUtils.getDocument(url);

        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        return models;
    }
}