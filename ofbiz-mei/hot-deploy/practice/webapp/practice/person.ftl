<#if persons?has_content>
  <div id="personList">
    <h2>Some of the people who visited our site are:</h2>
    <br>
    <ul>
      <#list persons as person>
        <li>${person.firstName!} ${person.lastName!}<li>
      </#list>
    </ul>
  </div>
</#if>
