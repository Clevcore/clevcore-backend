# v1.4.0
- New Release v1.4.0
- Add method setWildcardAll
- Rename the classes "Dao" by "Persistence"
- Move service.base to service package
- Move Exception Utils to Backend project
- Move ClevcoreException to Backend project
- Update maven version
- Ignore transient annotation type in "getEntityPropertyFromObject"
- Accept EmbeddedId object
- Use TypedQuery instead of Query
- Add (AND, OR) Condition in PersistenceUtils
- Update eclipselink and javax persistence version
- Create method "prepareWhere" for create where sql
- Close all entityManager in serviceImpl
- Remove mySql of pom.xml clevcore-backend
- Rename persistance file and dir for persistence
- Cleanup using SonarQube

# v1.3.0
- New Release v1.3.0
- Added slf4j-log4j support

# v1.2.0
- New Release v1.2.0
- BG-30: The getSelectQuery() method  in PersistanceUtils class don't wbe3af73 BG-27: In study data don't order the study field list when the action type is UPDATE
- BG-17: In persistence class, the alias of the tables are duplicate in some case

# V1.1.0
- New Release v1.1.0
- US-31: Clevcore-Engine migration to GitHub
- Initial commit
