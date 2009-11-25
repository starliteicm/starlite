@org.hibernate.annotations.TypeDefs({
  @org.hibernate.annotations.TypeDef(
    name="enum_varchar",
    typeClass=com.itao.persistence.EnumUserType.class
  )
})
package com.itao.starlite.model;