package app

object ExampleDatabase {
  val json =
    """{
    "tree" : {
      "items" : {
      "bf110e55-04a2-4051-b909-574645a4e5ba" : {
      "parentId" : {
      "id" : "2fffe3c7-62dd-4b90-a77f-311f41d4244d"
    },
      "id" : {
      "id" : "bf110e55-04a2-4051-b909-574645a4e5ba"
    },
      "text" : "Rechnung schreiben",
      "childrenIds" : [
      ],
      "deleted" : false,
      "expanded" : true
    },
      "66a705ca-88ce-4dce-b53c-23ef0009fd5f" : {
      "parentId" : {
      "id" : "5d4f5101-fbae-414a-8ca5-6be0bb71a825"
    },
      "id" : {
      "id" : "66a705ca-88ce-4dce-b53c-23ef0009fd5f"
    },
      "text" : "Fertig",
      "childrenIds" : [
    {
      "id" : "d91dfa98-b761-4ac7-af94-88d23227796d"
    }
      ],
      "deleted" : false,
      "expanded" : false
    },
      "19d1e1f5-f32f-4c7a-b681-b7fea012652a" : {
      "parentId" : {
      "id" : "3a08e93f-ae16-4055-8421-7b821c2625cf"
    },
      "id" : {
      "id" : "19d1e1f5-f32f-4c7a-b681-b7fea012652a"
    },
      "text" : "Arbeitsgruppen",
      "childrenIds" : [
      ],
      "deleted" : false,
      "expanded" : true
    },
      "98602348-7073-4a0a-8547-8c47f57cbdb4" : {
      "parentId" : {
      "id" : "a0a4fe1c-c6b9-48ba-b540-7edb56e7e83c"
    },
      "id" : {
      "id" : "98602348-7073-4a0a-8547-8c47f57cbdb4"
    },
      "text" : "Anwesend",
      "childrenIds" : [
    {
      "id" : "864edc65-e996-4fb2-918b-6bcbcf044385"
    },
    {
      "id" : "b7752b08-93fa-4c84-a4e4-0d39c2b71747"
    }
      ],
      "deleted" : false,
      "expanded" : false
    },
      "1b784fd6-379a-4a3f-9ff6-d79d47e281ed" : {
      "parentId" : {
      "id" : "5c72e75b-b750-436a-ae37-115c932eaaf2"
    },
      "id" : {
      "id" : "1b784fd6-379a-4a3f-9ff6-d79d47e281ed"
    },
      "text" : "Bugs",
      "childrenIds" : [
      ],
      "deleted" : false,
      "expanded" : true
    },
      "5bff6842-96ce-464f-b4c1-2771df5bf570" : {
      "parentId" : {
      "id" : "63f8d151-2267-4cd1-91f4-ceca2343791f"
    },
      "id" : {
      "id" : "5bff6842-96ce-464f-b4c1-2771df5bf570"
    },
      "text" : "Thomas Müller",
      "childrenIds" : [
    {
      "id" : "16a90331-fb99-4ce6-9907-4f8af9c78904"
    },
    {
      "id" : "70a71e78-3722-40e3-b98a-464e255a23e9"
    },
    {
      "id" : "5f341e15-fe28-4e97-a078-6d69d1acf99b"
    },
    {
      "id" : "b9665c9a-8c8e-4439-a492-3314d3d2e386"
    }
      ],
      "deleted" : false,
      "expanded" : false
    },
      "e1e8ffad-88aa-41d8-a53e-be5cad13f542" : {
      "parentId" : {
      "id" : "040bab7f-648a-4e4f-823e-6f686bcbf919"
    },
      "id" : {
      "id" : "e1e8ffad-88aa-41d8-a53e-be5cad13f542"
    },
      "text" : "Archiviert",
      "childrenIds" : [
    {
      "id" : "085d675d-e793-49ee-887f-632e553fdd88"
    },
    {
      "id" : "c4cd0c87-3e0a-4343-bc2c-5d8596fab4ab"
    }
      ],
      "deleted" : false,
      "expanded" : false
    },
      "ab521954-3719-4bd0-bbad-7ba3ab3110dd" : {
      "parentId" : {
      "id" : "a0a4fe1c-c6b9-48ba-b540-7edb56e7e83c"
    },
      "id" : {
      "id" : "ab521954-3719-4bd0-bbad-7ba3ab3110dd"
    },
      "text" : "Nächstes Treffen: 10.03. 14 Uhr",
      "childrenIds" : [
      ],
      "deleted" : false,
      "expanded" : true
    },
      "60edbd5c-5df4-4fb9-b84f-4dd0c07ada55" : {
      "parentId" : {
      "id" : "6730b568-a4af-420a-9948-c963d591e7c9"
    },
      "id" : {
      "id" : "60edbd5c-5df4-4fb9-b84f-4dd0c07ada55"
    },
      "text" : "10.03.2018",
      "childrenIds" : [
      ],
      "deleted" : false,
      "expanded" : true
    },
      "5d4f5101-fbae-414a-8ca5-6be0bb71a825" : {
      "parentId" : {
      "id" : "040bab7f-648a-4e4f-823e-6f686bcbf919"
    },
      "id" : {
      "id" : "5d4f5101-fbae-414a-8ca5-6be0bb71a825"
    },
      "text" : "Website erstellen",
      "childrenIds" : [
    {
      "id" : "66a705ca-88ce-4dce-b53c-23ef0009fd5f"
    },
    {
      "id" : "67fc20e0-264d-40a6-8f6a-bf758c45ab84"
    },
    {
      "id" : "2fffe3c7-62dd-4b90-a77f-311f41d4244d"
    }
      ],
      "deleted" : false,
      "expanded" : true
    },
      "3b44bd50-4b63-47b9-bbbb-8b3248b9ba20" : {
      "parentId" : {
      "id" : "b31c6baf-3e7f-430a-a61d-c8304170b6b2"
    },
      "id" : {
      "id" : "3b44bd50-4b63-47b9-bbbb-8b3248b9ba20"
    },
      "text" : "Ideen",
      "childrenIds" : [
      ],
      "deleted" : false,
      "expanded" : true
    },
      "d91dfa98-b761-4ac7-af94-88d23227796d" : {
      "parentId" : {
      "id" : "66a705ca-88ce-4dce-b53c-23ef0009fd5f"
    },
      "id" : {
      "id" : "d91dfa98-b761-4ac7-af94-88d23227796d"
    },
      "text" : "Domain registrieren",
      "childrenIds" : [
      ],
      "deleted" : false,
      "expanded" : true
    },
      "040bab7f-648a-4e4f-823e-6f686bcbf919" : {
      "parentId" : {
      "id" : "03e405e1-0750-418b-bf08-1cdb1d5bd25b"
    },
      "id" : {
      "id" : "040bab7f-648a-4e4f-823e-6f686bcbf919"
    },
      "text" : "Projekte",
      "childrenIds" : [
    {
      "id" : "5d4f5101-fbae-414a-8ca5-6be0bb71a825"
    },
    {
      "id" : "5c72e75b-b750-436a-ae37-115c932eaaf2"
    },
    {
      "id" : "e1e8ffad-88aa-41d8-a53e-be5cad13f542"
    }
      ],
      "deleted" : false,
      "expanded" : true
    },
      "63f8d151-2267-4cd1-91f4-ceca2343791f" : {
      "parentId" : {
      "id" : "3a08e93f-ae16-4055-8421-7b821c2625cf"
    },
      "id" : {
      "id" : "63f8d151-2267-4cd1-91f4-ceca2343791f"
    },
      "text" : "Kunden",
      "childrenIds" : [
    {
      "id" : "5bff6842-96ce-464f-b4c1-2771df5bf570"
    }
      ],
      "deleted" : false,
      "expanded" : false
    },
      "085d675d-e793-49ee-887f-632e553fdd88" : {
      "parentId" : {
      "id" : "e1e8ffad-88aa-41d8-a53e-be5cad13f542"
    },
      "id" : {
      "id" : "085d675d-e793-49ee-887f-632e553fdd88"
    },
      "text" : "Neue Mitarbeiterin finden",
      "childrenIds" : [
    {
      "id" : "51b99d78-851a-4b16-9d4c-10dd22f79f38"
    }
      ],
      "deleted" : false,
      "expanded" : false
    },
      "fe553705-7ab8-4386-824b-29bc744ca22e" : {
      "parentId" : {
      "id" : "67fc20e0-264d-40a6-8f6a-bf758c45ab84"
    },
      "id" : {
      "id" : "fe553705-7ab8-4386-824b-29bc744ca22e"
    },
      "text" : "Entwurf erstellen",
      "childrenIds" : [
      ],
      "deleted" : false,
      "expanded" : true
    },
      "51b99d78-851a-4b16-9d4c-10dd22f79f38" : {
      "parentId" : {
      "id" : "085d675d-e793-49ee-887f-632e553fdd88"
    },
      "id" : {
      "id" : "51b99d78-851a-4b16-9d4c-10dd22f79f38"
    },
      "text" : "...",
      "childrenIds" : [
      ],
      "deleted" : false,
      "expanded" : true
    },
      "43cb1577-54c3-4626-ba24-cb288766b002" : {
      "parentId" : {
      "id" : "111e94f9-b952-4f9f-a363-a67c0372f65f"
    },
      "id" : {
      "id" : "43cb1577-54c3-4626-ba24-cb288766b002"
    },
      "text" : "Er möchte ...",
      "childrenIds" : [
      ],
      "deleted" : false,
      "expanded" : true
    },
      "9fb0a278-0972-4b53-9c0c-a479fb1c88e8" : {
      "parentId" : {
      "id" : "b31c6baf-3e7f-430a-a61d-c8304170b6b2"
    },
      "id" : {
      "id" : "9fb0a278-0972-4b53-9c0c-a479fb1c88e8"
    },
      "text" : "Text",
      "childrenIds" : [
      ],
      "deleted" : false,
      "expanded" : true
    },
      "111e94f9-b952-4f9f-a363-a67c0372f65f" : {
      "parentId" : {
      "id" : "2fffe3c7-62dd-4b90-a77f-311f41d4244d"
    },
      "id" : {
      "id" : "111e94f9-b952-4f9f-a363-a67c0372f65f"
    },
      "text" : "Feedback des Kunden einarbeiten",
      "childrenIds" : [
    {
      "id" : "43cb1577-54c3-4626-ba24-cb288766b002"
    }
      ],
      "deleted" : false,
      "expanded" : false
    },
      "b7752b08-93fa-4c84-a4e4-0d39c2b71747" : {
      "parentId" : {
      "id" : "98602348-7073-4a0a-8547-8c47f57cbdb4"
    },
      "id" : {
      "id" : "b7752b08-93fa-4c84-a4e4-0d39c2b71747"
    },
      "text" : "Ilka",
      "childrenIds" : [
      ],
      "deleted" : false,
      "expanded" : true
    },
      "b9665c9a-8c8e-4439-a492-3314d3d2e386" : {
      "parentId" : {
      "id" : "5bff6842-96ce-464f-b4c1-2771df5bf570"
    },
      "id" : {
      "id" : "b9665c9a-8c8e-4439-a492-3314d3d2e386"
    },
      "text" : "Mobil: 01578 12 34 567",
      "childrenIds" : [
      ],
      "deleted" : false,
      "expanded" : true
    },
      "3a08e93f-ae16-4055-8421-7b821c2625cf" : {
      "parentId" : {
      "id" : "03e405e1-0750-418b-bf08-1cdb1d5bd25b"
    },
      "id" : {
      "id" : "3a08e93f-ae16-4055-8421-7b821c2625cf"
    },
      "text" : "Wissen",
      "childrenIds" : [
    {
      "id" : "19d1e1f5-f32f-4c7a-b681-b7fea012652a"
    },
    {
      "id" : "63f8d151-2267-4cd1-91f4-ceca2343791f"
    },
    {
      "id" : "6730b568-a4af-420a-9948-c963d591e7c9"
    },
    {
      "id" : "3febd899-0b30-423e-8df9-581522e13b2c"
    }
      ],
      "deleted" : false,
      "expanded" : true
    },
      "864edc65-e996-4fb2-918b-6bcbcf044385" : {
      "parentId" : {
      "id" : "98602348-7073-4a0a-8547-8c47f57cbdb4"
    },
      "id" : {
      "id" : "864edc65-e996-4fb2-918b-6bcbcf044385"
    },
      "text" : "Jan",
      "childrenIds" : [
      ],
      "deleted" : false,
      "expanded" : true
    },
      "5c72e75b-b750-436a-ae37-115c932eaaf2" : {
      "parentId" : {
      "id" : "040bab7f-648a-4e4f-823e-6f686bcbf919"
    },
      "id" : {
      "id" : "5c72e75b-b750-436a-ae37-115c932eaaf2"
    },
      "text" : "Mobile App entwickeln",
      "childrenIds" : [
    {
      "id" : "0cf7bab1-5bf0-447e-aa70-cb223c8bb040"
    },
    {
      "id" : "1b784fd6-379a-4a3f-9ff6-d79d47e281ed"
    },
    {
      "id" : "b2cc51d6-b53a-492c-92b0-4e6df277aab3"
    }
      ],
      "deleted" : false,
      "expanded" : false
    },
      "a0a4fe1c-c6b9-48ba-b540-7edb56e7e83c" : {
      "parentId" : {
      "id" : "6730b568-a4af-420a-9948-c963d591e7c9"
    },
      "id" : {
      "id" : "a0a4fe1c-c6b9-48ba-b540-7edb56e7e83c"
    },
      "text" : "10.02.2018",
      "childrenIds" : [
    {
      "id" : "98602348-7073-4a0a-8547-8c47f57cbdb4"
    },
    {
      "id" : "ab521954-3719-4bd0-bbad-7ba3ab3110dd"
    },
    {
      "id" : "b31c6baf-3e7f-430a-a61d-c8304170b6b2"
    },
    {
      "id" : "1f6e75e8-285a-4153-b0f8-b82969b7b4e2"
    }
      ],
      "deleted" : false,
      "expanded" : false
    },
      "c4cd0c87-3e0a-4343-bc2c-5d8596fab4ab" : {
      "parentId" : {
      "id" : "e1e8ffad-88aa-41d8-a53e-be5cad13f542"
    },
      "id" : {
      "id" : "c4cd0c87-3e0a-4343-bc2c-5d8596fab4ab"
    },
      "text" : "Projektförderer finden",
      "childrenIds" : [
      ],
      "deleted" : false,
      "expanded" : true
    },
      "ea8f7e44-7772-4a05-bab2-c154117c931b" : {
      "parentId" : {
      "id" : "0ad834bc-2f91-4dee-b954-b062c9b5bec8"
    },
      "id" : {
      "id" : "ea8f7e44-7772-4a05-bab2-c154117c931b"
    },
      "text" : "App dem Kunden vorstellen",
      "childrenIds" : [
      ],
      "deleted" : false,
      "expanded" : true
    },
      "41ac39b8-389a-49aa-9a8e-4e3393a33246" : {
      "parentId" : {
      "id" : "b31c6baf-3e7f-430a-a61d-c8304170b6b2"
    },
      "id" : {
      "id" : "41ac39b8-389a-49aa-9a8e-4e3393a33246"
    },
      "text" : "Wo drucken?",
      "childrenIds" : [
      ],
      "deleted" : false,
      "expanded" : true
    },
      "f6100fd7-992b-4837-99f8-cfe26c941cc4" : {
      "parentId" : {
      "id" : "b31c6baf-3e7f-430a-a61d-c8304170b6b2"
    },
      "id" : {
      "id" : "f6100fd7-992b-4837-99f8-cfe26c941cc4"
    },
      "text" : "Logo",
      "childrenIds" : [
      ],
      "deleted" : false,
      "expanded" : true
    },
      "70a71e78-3722-40e3-b98a-464e255a23e9" : {
      "parentId" : {
      "id" : "5bff6842-96ce-464f-b4c1-2771df5bf570"
    },
      "id" : {
      "id" : "70a71e78-3722-40e3-b98a-464e255a23e9"
    },
      "text" : "Kennengelernt über Sabine",
      "childrenIds" : [
      ],
      "deleted" : false,
      "expanded" : true
    },
      "6730b568-a4af-420a-9948-c963d591e7c9" : {
      "parentId" : {
      "id" : "3a08e93f-ae16-4055-8421-7b821c2625cf"
    },
      "id" : {
      "id" : "6730b568-a4af-420a-9948-c963d591e7c9"
    },
      "text" : "Protokolle",
      "childrenIds" : [
    {
      "id" : "a0a4fe1c-c6b9-48ba-b540-7edb56e7e83c"
    },
    {
      "id" : "60edbd5c-5df4-4fb9-b84f-4dd0c07ada55"
    }
      ],
      "deleted" : false,
      "expanded" : false
    },
      "0ad834bc-2f91-4dee-b954-b062c9b5bec8" : {
      "parentId" : {
      "id" : "b2cc51d6-b53a-492c-92b0-4e6df277aab3"
    },
      "id" : {
      "id" : "0ad834bc-2f91-4dee-b954-b062c9b5bec8"
    },
      "text" : "Juni 2018",
      "childrenIds" : [
    {
      "id" : "ea8f7e44-7772-4a05-bab2-c154117c931b"
    }
      ],
      "deleted" : false,
      "expanded" : false
    },
      "5f341e15-fe28-4e97-a078-6d69d1acf99b" : {
      "parentId" : {
      "id" : "5bff6842-96ce-464f-b4c1-2771df5bf570"
    },
      "id" : {
      "id" : "5f341e15-fe28-4e97-a078-6d69d1acf99b"
    },
      "text" : "thomas.mueller@mail.de",
      "childrenIds" : [
      ],
      "deleted" : false,
      "expanded" : true
    },
      "b31c6baf-3e7f-430a-a61d-c8304170b6b2" : {
      "parentId" : {
      "id" : "a0a4fe1c-c6b9-48ba-b540-7edb56e7e83c"
    },
      "id" : {
      "id" : "b31c6baf-3e7f-430a-a61d-c8304170b6b2"
    },
      "text" : "TOP Flyer",
      "childrenIds" : [
    {
      "id" : "41ac39b8-389a-49aa-9a8e-4e3393a33246"
    },
    {
      "id" : "9fb0a278-0972-4b53-9c0c-a479fb1c88e8"
    },
    {
      "id" : "f6100fd7-992b-4837-99f8-cfe26c941cc4"
    },
    {
      "id" : "3b44bd50-4b63-47b9-bbbb-8b3248b9ba20"
    }
      ],
      "deleted" : false,
      "expanded" : false
    },
      "67fc20e0-264d-40a6-8f6a-bf758c45ab84" : {
      "parentId" : {
      "id" : "5d4f5101-fbae-414a-8ca5-6be0bb71a825"
    },
      "id" : {
      "id" : "67fc20e0-264d-40a6-8f6a-bf758c45ab84"
    },
      "text" : "In Bearbeitung",
      "childrenIds" : [
    {
      "id" : "fe553705-7ab8-4386-824b-29bc744ca22e"
    }
      ],
      "deleted" : false,
      "expanded" : true
    },
      "4e0665d5-6be5-46a3-86aa-ab33109624dd" : {
      "parentId" : {
      "id" : "b2cc51d6-b53a-492c-92b0-4e6df277aab3"
    },
      "id" : {
      "id" : "4e0665d5-6be5-46a3-86aa-ab33109624dd"
    },
      "text" : "Juli 2018",
      "childrenIds" : [
      ],
      "deleted" : false,
      "expanded" : true
    },
      "3febd899-0b30-423e-8df9-581522e13b2c" : {
      "parentId" : {
      "id" : "3a08e93f-ae16-4055-8421-7b821c2625cf"
    },
      "id" : {
      "id" : "3febd899-0b30-423e-8df9-581522e13b2c"
    },
      "text" : "Unternehmensziele",
      "childrenIds" : [
      ],
      "deleted" : false,
      "expanded" : true
    },
      "b2cc51d6-b53a-492c-92b0-4e6df277aab3" : {
      "parentId" : {
      "id" : "5c72e75b-b750-436a-ae37-115c932eaaf2"
    },
      "id" : {
      "id" : "b2cc51d6-b53a-492c-92b0-4e6df277aab3"
    },
      "text" : "Meilensteine",
      "childrenIds" : [
    {
      "id" : "0ad834bc-2f91-4dee-b954-b062c9b5bec8"
    },
    {
      "id" : "4e0665d5-6be5-46a3-86aa-ab33109624dd"
    }
      ],
      "deleted" : false,
      "expanded" : false
    },
      "03e405e1-0750-418b-bf08-1cdb1d5bd25b" : {
      "parentId" : {
      "id" : "f52ac4f6-0aca-47f8-b9cc-f4a89599b005"
    },
      "id" : {
      "id" : "03e405e1-0750-418b-bf08-1cdb1d5bd25b"
    },
      "text" : "",
      "childrenIds" : [
    {
      "id" : "040bab7f-648a-4e4f-823e-6f686bcbf919"
    },
    {
      "id" : "3a08e93f-ae16-4055-8421-7b821c2625cf"
    }
      ],
      "deleted" : false,
      "expanded" : true
    },
      "0cf7bab1-5bf0-447e-aa70-cb223c8bb040" : {
      "parentId" : {
      "id" : "5c72e75b-b750-436a-ae37-115c932eaaf2"
    },
      "id" : {
      "id" : "0cf7bab1-5bf0-447e-aa70-cb223c8bb040"
    },
      "text" : "Features",
      "childrenIds" : [
      ],
      "deleted" : false,
      "expanded" : true
    },
      "16a90331-fb99-4ce6-9907-4f8af9c78904" : {
      "parentId" : {
      "id" : "5bff6842-96ce-464f-b4c1-2771df5bf570"
    },
      "id" : {
      "id" : "16a90331-fb99-4ce6-9907-4f8af9c78904"
    },
      "text" : "Rechtsanwalt",
      "childrenIds" : [
      ],
      "deleted" : false,
      "expanded" : true
    },
      "2fffe3c7-62dd-4b90-a77f-311f41d4244d" : {
      "parentId" : {
      "id" : "5d4f5101-fbae-414a-8ca5-6be0bb71a825"
    },
      "id" : {
      "id" : "2fffe3c7-62dd-4b90-a77f-311f41d4244d"
    },
      "text" : "Später",
      "childrenIds" : [
    {
      "id" : "111e94f9-b952-4f9f-a363-a67c0372f65f"
    },
    {
      "id" : "bf110e55-04a2-4051-b909-574645a4e5ba"
    }
      ],
      "deleted" : false,
      "expanded" : false
    },
      "1f6e75e8-285a-4153-b0f8-b82969b7b4e2" : {
      "parentId" : {
      "id" : "a0a4fe1c-c6b9-48ba-b540-7edb56e7e83c"
    },
      "id" : {
      "id" : "1f6e75e8-285a-4153-b0f8-b82969b7b4e2"
    },
      "text" : "TOP Geschäftsmodel",
      "childrenIds" : [
      ],
      "deleted" : false,
      "expanded" : true
    }
    }
    },
    "selected" : {
      "id" : "67fc20e0-264d-40a6-8f6a-bf758c45ab84"
    },
    "editing" : null,
    "instructions" : {
      "upDown" : {
      "text" : "Wandere mit den Pfeiltasten <kbd><i class=\"fas fa-arrow-up fa-xs\"></i></kbd> und <kbd><i class=\"fas fa-arrow-down fa-xs\"></i></kbd> durch die Einträge.",
      "completed" : false
    },
      "right" : {
      "text" : "Klappe mit <kbd><i class=\"fas fa-arrow-right fa-xs\"></i></kbd> den selektierten Eintrag aus.",
      "completed" : false
    },
      "left" : {
      "text" : "Springe mit <kbd><i class=\"fas fa-arrow-left fa-xs\"></i></kbd> zum Eltern-Eintrag.",
      "completed" : false
    },
      "tabEdit" : {
      "text" : "Bearbeite den Text, indem du <kbd>Tab</kbd> drückst.",
      "completed" : false
    },
      "clickEdit" : {
      "text" : "Oder indem du einen Eintrag doppelklickst.",
      "completed" : false
    },
      "create" : {
      "text" : "Erstelle einen Eintrag mit <kbd>Enter</kbd>.",
      "completed" : false
    },
      "createChild" : {
      "text" : "Erstelle einen Unter-Eintrag mit <kbd>Shift</kbd> + <kbd>Enter</kbd>.",
      "completed" : false
    },
      "delete" : {
      "text" : "Lösche den selektierten Eintrag mit <kbd>Entf</kbd>.",
      "completed" : false
    },
      "moveVertically" : {
      "text" : "Bewege den selektierten Eintrag mit <kbd>W</kbd> und <kbd>S</kbd> nach oben und unten.",
      "completed" : false
    },
      "moveRight" : {
      "text" : "<kbd>D</kbd> verschiebt den selektierten Eintrag eine Ebene tiefer, in diese Richtung <i class=\"fas fa-hand-point-right\"></i>.",
      "completed" : false
    },
      "moveLeft" : {
      "text" : "<kbd>A</kbd> verschiebt den selektierten Eintrag eine Ebene höher, in diese Richtung <i class=\"fas fa-hand-point-left\"></i>.",
      "completed" : false
    }
    },
    "lastSelectDirection" : {
      "Before" : {

    }
    }
  }"""
}
