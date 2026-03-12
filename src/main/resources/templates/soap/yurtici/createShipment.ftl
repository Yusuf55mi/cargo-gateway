<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ship="http://yurticikargo.com.tr/ShippingOrderDispatcherServices">
    <soapenv:Header/>
    <soapenv:Body>
        <ship:createShipment>
            <wsUserName>${username}</wsUserName>
            <wsPassword>${password}</wsPassword>
            <userLanguage>${userLanguage}</userLanguage>
            <ShippingOrderVO>
                <cargoKey>${cargoKey}</cargoKey>
                <invoiceKey>${referenceNo}</invoiceKey>
                <cargoCount>${cargoCount}</cargoCount>
                <desi>${desi}</desi>
                <receiverCustName>${receiver.firstName} ${receiver.lastName}</receiverCustName>
                <receiverAddress>${receiver.address}</receiverAddress>
                <receiverPhone1>${receiver.phone}</receiverPhone1>
                <cityName>${receiver.city}</cityName>
                <townName>${receiver.district}</townName>
<#if hasCOD>
                <ttCollectionType>${ttCollectionType}</ttCollectionType>
                <ttInvoiceAmount>${ttCollectionPrice}</ttInvoiceAmount>
                <ttDocumentId>${cargoKey}</ttDocumentId>
                <ttDocumentSaveType>0</ttDocumentSaveType>
</#if>
            </ShippingOrderVO>
        </ship:createShipment>
    </soapenv:Body>
</soapenv:Envelope>