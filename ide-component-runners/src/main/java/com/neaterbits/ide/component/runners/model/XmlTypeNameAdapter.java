package com.neaterbits.ide.component.runners.model;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.neaterbits.build.types.TypeName;

public class XmlTypeNameAdapter extends XmlAdapter<XmlTypeName, TypeName> {

    @Override
    public TypeName unmarshal(XmlTypeName v) throws Exception {

        return new TypeName(v.getNamespace(), v.getOuterTypes(), v.getName());
    }

    @Override
    public XmlTypeName marshal(TypeName v) throws Exception {
        return new XmlTypeName(v);
    }
}