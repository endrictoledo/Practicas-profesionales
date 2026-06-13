-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: sistemapracticasprofesionalesbd
-- ------------------------------------------------------
-- Server version	9.4.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `asignacion`
--

DROP TABLE IF EXISTS `asignacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asignacion` (
  `id_proyecto_fk` int NOT NULL,
  `Estudiante_idEstudiante` int NOT NULL,
  `fechaInicio` datetime NOT NULL,
  `fechaFinal` datetime NOT NULL,
  PRIMARY KEY (`id_proyecto_fk`,`Estudiante_idEstudiante`),
  KEY `id_proyecto_fk_idx` (`id_proyecto_fk`),
  KEY `fk_Asignacion_Estudiante1_idx` (`Estudiante_idEstudiante`),
  CONSTRAINT `fk_Asignacion_Estudiante1` FOREIGN KEY (`Estudiante_idEstudiante`) REFERENCES `estudiante` (`idEstudiante`),
  CONSTRAINT `id_proyecto_fk` FOREIGN KEY (`id_proyecto_fk`) REFERENCES `proyecto` (`idProyecto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asignacion`
--

LOCK TABLES `asignacion` WRITE;
/*!40000 ALTER TABLE `asignacion` DISABLE KEYS */;
/*!40000 ALTER TABLE `asignacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `catalogo_documento`
--

DROP TABLE IF EXISTS `catalogo_documento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `catalogo_documento` (
  `idCatalogoDocumento` int NOT NULL AUTO_INCREMENT,
  `nombreDocumento` varchar(45) NOT NULL,
  PRIMARY KEY (`idCatalogoDocumento`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `catalogo_documento`
--

LOCK TABLES `catalogo_documento` WRITE;
/*!40000 ALTER TABLE `catalogo_documento` DISABLE KEYS */;
/*!40000 ALTER TABLE `catalogo_documento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `documento`
--

DROP TABLE IF EXISTS `documento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `documento` (
  `idDocumento` int NOT NULL AUTO_INCREMENT,
  `fecha` date NOT NULL,
  `Calificacion` varchar(45) DEFAULT NULL,
  `Estado_documento_idEstado_documento` int NOT NULL,
  `Catalogo_documento_idCatalogo_documento` int NOT NULL,
  `Expediente_id_expediente` int NOT NULL,
  PRIMARY KEY (`idDocumento`),
  KEY `fk_Documento_Catalogo_documento1_idx` (`Catalogo_documento_idCatalogo_documento`),
  KEY `fk_Documento_Expediente1_idx` (`Expediente_id_expediente`),
  KEY `fk_Documento_Estado_documento1_idx` (`Estado_documento_idEstado_documento`),
  CONSTRAINT `fk_Documento_Catalogo_documento1` FOREIGN KEY (`Catalogo_documento_idCatalogo_documento`) REFERENCES `catalogo_documento` (`idCatalogoDocumento`),
  CONSTRAINT `fk_Documento_Estado_documento1` FOREIGN KEY (`Estado_documento_idEstado_documento`) REFERENCES `estado_documento` (`idEstadoDocumento`),
  CONSTRAINT `fk_Documento_Expediente1` FOREIGN KEY (`Expediente_id_expediente`) REFERENCES `expediente` (`idExpediente`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `documento`
--

LOCK TABLES `documento` WRITE;
/*!40000 ALTER TABLE `documento` DISABLE KEYS */;
/*!40000 ALTER TABLE `documento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `encargadoproyecto`
--

DROP TABLE IF EXISTS `encargadoproyecto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `encargadoproyecto` (
  `idEncargadoProyecto` int NOT NULL AUTO_INCREMENT,
  `nombreEncargado` varchar(20) NOT NULL,
  `apellidoPaterno` varchar(20) NOT NULL,
  `apellidoMaterno` varchar(20) NOT NULL,
  `cargo` varchar(45) NOT NULL,
  `correoElectronico` varchar(45) NOT NULL,
  `Organizacion_vinculada_idOrganizacionVinculada` int NOT NULL,
  PRIMARY KEY (`idEncargadoProyecto`),
  KEY `fk_EncargadoProyecto_Organizacion_vinculada1_idx` (`Organizacion_vinculada_idOrganizacionVinculada`),
  CONSTRAINT `fk_EncargadoProyecto_Organizacion_vinculada1` FOREIGN KEY (`Organizacion_vinculada_idOrganizacionVinculada`) REFERENCES `organizacion_vinculada` (`idOrganizacionVinculada`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `encargadoproyecto`
--

LOCK TABLES `encargadoproyecto` WRITE;
/*!40000 ALTER TABLE `encargadoproyecto` DISABLE KEYS */;
INSERT INTO `encargadoproyecto` VALUES (1,'María','Sánchez','López','Jefa de Desarrollo','msanchez@uv.mx',1),(2,'María','Sánchez','López','Jefa de Desarrollo','msanchez@uv.mx',1),(3,'María','Sánchez','López','Jefa de Desarrollo','msanchez@uv.mx',1),(4,'María','Sánchez','López','Jefa de Desarrollo','msanchez@uv.mx',1),(5,'María','Sánchez','López','Jefa de Desarrollo','msanchez@uv.mx',1);
/*!40000 ALTER TABLE `encargadoproyecto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estado_documento`
--

DROP TABLE IF EXISTS `estado_documento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estado_documento` (
  `idEstadoDocumento` int NOT NULL AUTO_INCREMENT,
  `nombreEstado` varchar(45) NOT NULL,
  PRIMARY KEY (`idEstadoDocumento`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estado_documento`
--

LOCK TABLES `estado_documento` WRITE;
/*!40000 ALTER TABLE `estado_documento` DISABLE KEYS */;
/*!40000 ALTER TABLE `estado_documento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estado_estudiante`
--

DROP TABLE IF EXISTS `estado_estudiante`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estado_estudiante` (
  `idEstadoEstudiante` int NOT NULL AUTO_INCREMENT,
  `nomobreEstado` varchar(45) NOT NULL,
  PRIMARY KEY (`idEstadoEstudiante`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estado_estudiante`
--

LOCK TABLES `estado_estudiante` WRITE;
/*!40000 ALTER TABLE `estado_estudiante` DISABLE KEYS */;
INSERT INTO `estado_estudiante` VALUES (1,'ACTIVO'),(2,'BAJA DEFINITIVA');
/*!40000 ALTER TABLE `estado_estudiante` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estado_expediente`
--

DROP TABLE IF EXISTS `estado_expediente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estado_expediente` (
  `idEstadoExpediente` int NOT NULL AUTO_INCREMENT,
  `nombreEstado` varchar(45) NOT NULL,
  PRIMARY KEY (`idEstadoExpediente`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estado_expediente`
--

LOCK TABLES `estado_expediente` WRITE;
/*!40000 ALTER TABLE `estado_expediente` DISABLE KEYS */;
/*!40000 ALTER TABLE `estado_expediente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estado_personal`
--

DROP TABLE IF EXISTS `estado_personal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estado_personal` (
  `idEstadoPersonal` int NOT NULL AUTO_INCREMENT,
  `nombreEstado` varchar(45) NOT NULL,
  PRIMARY KEY (`idEstadoPersonal`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estado_personal`
--

LOCK TABLES `estado_personal` WRITE;
/*!40000 ALTER TABLE `estado_personal` DISABLE KEYS */;
INSERT INTO `estado_personal` VALUES (1,'ACTIVO'),(2,'INACTIVO');
/*!40000 ALTER TABLE `estado_personal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estado_usuario`
--

DROP TABLE IF EXISTS `estado_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estado_usuario` (
  `idEstadoUsuario` int NOT NULL AUTO_INCREMENT,
  `nombreEstado` varchar(20) NOT NULL,
  PRIMARY KEY (`idEstadoUsuario`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estado_usuario`
--

LOCK TABLES `estado_usuario` WRITE;
/*!40000 ALTER TABLE `estado_usuario` DISABLE KEYS */;
INSERT INTO `estado_usuario` VALUES (1,'ACTIVO'),(2,'INACTIVO');
/*!40000 ALTER TABLE `estado_usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estudiante`
--

DROP TABLE IF EXISTS `estudiante`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estudiante` (
  `idEstudiante` int NOT NULL AUTO_INCREMENT,
  `matricula` varchar(9) NOT NULL,
  `nombreEstudiante` varchar(45) NOT NULL,
  `avanceCrediticio` int NOT NULL,
  `promedio` decimal(4,2) NOT NULL,
  `Usuario_idUsuario` int NOT NULL,
  `Seccion_EE_idSeccion_EE` int NOT NULL,
  `Estado_Estudiante_idEstado_Estudiante` int NOT NULL,
  PRIMARY KEY (`idEstudiante`),
  UNIQUE KEY `matricula_UNIQUE` (`matricula`),
  KEY `fk_Estudiante_Usuario1_idx` (`Usuario_idUsuario`),
  KEY `fk_Estudiante_Seccion_EE1_idx` (`Seccion_EE_idSeccion_EE`),
  KEY `fk_Estudiante_Estado_Estudiante1_idx` (`Estado_Estudiante_idEstado_Estudiante`),
  CONSTRAINT `fk_Estudiante_Estado_Estudiante1` FOREIGN KEY (`Estado_Estudiante_idEstado_Estudiante`) REFERENCES `estado_estudiante` (`idEstadoEstudiante`),
  CONSTRAINT `fk_Estudiante_Seccion_EE1` FOREIGN KEY (`Seccion_EE_idSeccion_EE`) REFERENCES `seccion_ee` (`idSeccionEE`),
  CONSTRAINT `fk_Estudiante_Usuario1` FOREIGN KEY (`Usuario_idUsuario`) REFERENCES `usuario` (`idUsuario`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estudiante`
--

LOCK TABLES `estudiante` WRITE;
/*!40000 ALTER TABLE `estudiante` DISABLE KEYS */;
INSERT INTO `estudiante` VALUES (11,'S19012345','Endric',85,9.20,1,11,1);
/*!40000 ALTER TABLE `estudiante` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expediente`
--

DROP TABLE IF EXISTS `expediente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `expediente` (
  `idExpediente` int NOT NULL AUTO_INCREMENT,
  `Estado_Expediente_idEstado_Expediente` int NOT NULL,
  `Estudiante_idEstudiante` int NOT NULL,
  PRIMARY KEY (`idExpediente`),
  KEY `fk_Expediente_Estado_Expediente1_idx` (`Estado_Expediente_idEstado_Expediente`),
  KEY `fk_Expediente_Estudiante1_idx` (`Estudiante_idEstudiante`),
  CONSTRAINT `fk_Expediente_Estado_Expediente1` FOREIGN KEY (`Estado_Expediente_idEstado_Expediente`) REFERENCES `estado_expediente` (`idEstadoExpediente`),
  CONSTRAINT `fk_Expediente_Estudiante1` FOREIGN KEY (`Estudiante_idEstudiante`) REFERENCES `estudiante` (`idEstudiante`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expediente`
--

LOCK TABLES `expediente` WRITE;
/*!40000 ALTER TABLE `expediente` DISABLE KEYS */;
/*!40000 ALTER TABLE `expediente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `experiencia_educativa`
--

DROP TABLE IF EXISTS `experiencia_educativa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `experiencia_educativa` (
  `idExperienciaEducativa` int NOT NULL AUTO_INCREMENT,
  `nombreExperiencia` varchar(45) NOT NULL,
  `nrc` varchar(10) NOT NULL,
  `Seccion_EE_idSeccionEE` int NOT NULL,
  PRIMARY KEY (`idExperienciaEducativa`),
  KEY `fk_Experiencia_Educativa_Seccion_EE1_idx` (`Seccion_EE_idSeccionEE`),
  CONSTRAINT `fk_Experiencia_Educativa_Seccion_EE1` FOREIGN KEY (`Seccion_EE_idSeccionEE`) REFERENCES `seccion_ee` (`idSeccionEE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `experiencia_educativa`
--

LOCK TABLES `experiencia_educativa` WRITE;
/*!40000 ALTER TABLE `experiencia_educativa` DISABLE KEYS */;
/*!40000 ALTER TABLE `experiencia_educativa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mensaje`
--

DROP TABLE IF EXISTS `mensaje`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mensaje` (
  `idMensaje` int NOT NULL AUTO_INCREMENT,
  `CorreoDestinatario` varchar(45) NOT NULL,
  `CorreoRemitente` varchar(45) NOT NULL,
  `Proposito` varchar(45) NOT NULL,
  `Mensaje` varchar(200) NOT NULL,
  `Usuario_idUsuario` int NOT NULL,
  PRIMARY KEY (`idMensaje`),
  KEY `fk_Mensaje_Usuario1_idx` (`Usuario_idUsuario`),
  CONSTRAINT `fk_Mensaje_Usuario1` FOREIGN KEY (`Usuario_idUsuario`) REFERENCES `usuario` (`idUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mensaje`
--

LOCK TABLES `mensaje` WRITE;
/*!40000 ALTER TABLE `mensaje` DISABLE KEYS */;
/*!40000 ALTER TABLE `mensaje` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organizacion_vinculada`
--

DROP TABLE IF EXISTS `organizacion_vinculada`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `organizacion_vinculada` (
  `idOrganizacionVinculada` int NOT NULL AUTO_INCREMENT,
  `razonSocial` varchar(45) NOT NULL,
  `direccion` varchar(45) NOT NULL,
  `sector` varchar(15) NOT NULL,
  PRIMARY KEY (`idOrganizacionVinculada`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organizacion_vinculada`
--

LOCK TABLES `organizacion_vinculada` WRITE;
/*!40000 ALTER TABLE `organizacion_vinculada` DISABLE KEYS */;
INSERT INTO `organizacion_vinculada` VALUES (1,'UV DGTIC','Zona Universitaria Xalapa','Público');
/*!40000 ALTER TABLE `organizacion_vinculada` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `periodo_escolar`
--

DROP TABLE IF EXISTS `periodo_escolar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `periodo_escolar` (
  `idPeriodoEscolar` int NOT NULL AUTO_INCREMENT,
  `fechaInicio` date NOT NULL,
  `fechaFin` date NOT NULL,
  `descripcion` varchar(50) NOT NULL,
  PRIMARY KEY (`idPeriodoEscolar`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `periodo_escolar`
--

LOCK TABLES `periodo_escolar` WRITE;
/*!40000 ALTER TABLE `periodo_escolar` DISABLE KEYS */;
INSERT INTO `periodo_escolar` VALUES (6,'2026-02-01','2026-07-31','FEB-JUL 2026');
/*!40000 ALTER TABLE `periodo_escolar` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `personal_practicas`
--

DROP TABLE IF EXISTS `personal_practicas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `personal_practicas` (
  `idProfesor` int NOT NULL AUTO_INCREMENT,
  `cubiculo` varchar(45) NOT NULL,
  `nombreProfesor` varchar(45) NOT NULL,
  `apellidoPaterno` varchar(45) NOT NULL,
  `apellidoMaterno` varchar(45) DEFAULT NULL,
  `numeroPersonal` varchar(20) NOT NULL,
  `Usuario_idUsuario` int NOT NULL,
  `Estado_Personal_idEstado_Personal` int NOT NULL,
  PRIMARY KEY (`idProfesor`),
  KEY `fk_Profesor_practicas_Usuario1_idx` (`Usuario_idUsuario`),
  KEY `fk_Personal_practicas_Estado_Personal1_idx` (`Estado_Personal_idEstado_Personal`),
  CONSTRAINT `fk_Personal_practicas_Estado_Personal1` FOREIGN KEY (`Estado_Personal_idEstado_Personal`) REFERENCES `estado_personal` (`idEstadoPersonal`),
  CONSTRAINT `fk_Profesor_practicas_Usuario1` FOREIGN KEY (`Usuario_idUsuario`) REFERENCES `usuario` (`idUsuario`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `personal_practicas`
--

LOCK TABLES `personal_practicas` WRITE;
/*!40000 ALTER TABLE `personal_practicas` DISABLE KEYS */;
INSERT INTO `personal_practicas` VALUES (6,'Cubículo 10','Carlos','Pérez','García','12345',5,1),(7,'Cubículo 10','Carlos','Pérez','García','12345',5,1);
/*!40000 ALTER TABLE `personal_practicas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `personal_tiene_rol`
--

DROP TABLE IF EXISTS `personal_tiene_rol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `personal_tiene_rol` (
  `Rol_idRol` int NOT NULL,
  `Personal_practicas_id_proefesor` int NOT NULL,
  PRIMARY KEY (`Rol_idRol`,`Personal_practicas_id_proefesor`),
  KEY `fk_Personal_tiene_Rol_Personal_practicas1_idx` (`Personal_practicas_id_proefesor`),
  CONSTRAINT `fk_Personal_tiene_Rol_Personal_practicas1` FOREIGN KEY (`Personal_practicas_id_proefesor`) REFERENCES `personal_practicas` (`idProfesor`),
  CONSTRAINT `fk_Personal_tiene_Rol_Rol1` FOREIGN KEY (`Rol_idRol`) REFERENCES `rol` (`idRol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `personal_tiene_rol`
--

LOCK TABLES `personal_tiene_rol` WRITE;
/*!40000 ALTER TABLE `personal_tiene_rol` DISABLE KEYS */;
INSERT INTO `personal_tiene_rol` VALUES (2,6);
/*!40000 ALTER TABLE `personal_tiene_rol` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proyecto`
--

DROP TABLE IF EXISTS `proyecto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `proyecto` (
  `idProyecto` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `descripcion` varchar(255) NOT NULL,
  `objetivo` varchar(45) NOT NULL,
  `Organizacion_vinculada_id_organizacion_vinculada` int NOT NULL,
  `EncargadoProyecto_idEncargadoProyecto` int NOT NULL,
  PRIMARY KEY (`idProyecto`),
  KEY `fk_Proyecto_Organizacion_vinculada1_idx` (`Organizacion_vinculada_id_organizacion_vinculada`),
  KEY `fk_Proyecto_EncargadoProyecto1_idx` (`EncargadoProyecto_idEncargadoProyecto`),
  CONSTRAINT `fk_Proyecto_EncargadoProyecto1` FOREIGN KEY (`EncargadoProyecto_idEncargadoProyecto`) REFERENCES `encargadoproyecto` (`idEncargadoProyecto`),
  CONSTRAINT `fk_Proyecto_Organizacion_vinculada1` FOREIGN KEY (`Organizacion_vinculada_id_organizacion_vinculada`) REFERENCES `organizacion_vinculada` (`idOrganizacionVinculada`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proyecto`
--

LOCK TABLES `proyecto` WRITE;
/*!40000 ALTER TABLE `proyecto` DISABLE KEYS */;
INSERT INTO `proyecto` VALUES (1,'Desarrollo Web Institucional','Apoyo en la modernización de sistemas web usando Java.','Implementar módulos seguros.',1,1);
/*!40000 ALTER TABLE `proyecto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rol`
--

DROP TABLE IF EXISTS `rol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rol` (
  `idRol` int NOT NULL AUTO_INCREMENT,
  `nombreRol` varchar(45) NOT NULL,
  PRIMARY KEY (`idRol`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rol`
--

LOCK TABLES `rol` WRITE;
/*!40000 ALTER TABLE `rol` DISABLE KEYS */;
INSERT INTO `rol` VALUES (1,'ADMINISTRADOR'),(2,'PROFESOR'),(3,'COORDINADOR');
/*!40000 ALTER TABLE `rol` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seccion_ee`
--

DROP TABLE IF EXISTS `seccion_ee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `seccion_ee` (
  `idSeccionEE` int NOT NULL AUTO_INCREMENT,
  `nrcEE` varchar(5) NOT NULL,
  `Personal_practicas_id_proefesor` int NOT NULL,
  `Periodo_escolar_idPeriodo_escolar` int NOT NULL,
  PRIMARY KEY (`idSeccionEE`),
  KEY `fk_Seccion_EE_Personal_practicas1_idx` (`Personal_practicas_id_proefesor`),
  KEY `fk_Seccion_EE_Periodo_escolar1_idx` (`Periodo_escolar_idPeriodo_escolar`),
  CONSTRAINT `fk_Seccion_EE_Periodo_escolar1` FOREIGN KEY (`Periodo_escolar_idPeriodo_escolar`) REFERENCES `periodo_escolar` (`idPeriodoEscolar`),
  CONSTRAINT `fk_Seccion_EE_Personal_practicas1` FOREIGN KEY (`Personal_practicas_id_proefesor`) REFERENCES `personal_practicas` (`idProfesor`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seccion_ee`
--

LOCK TABLES `seccion_ee` WRITE;
/*!40000 ALTER TABLE `seccion_ee` DISABLE KEYS */;
INSERT INTO `seccion_ee` VALUES (11,'84752',6,6);
/*!40000 ALTER TABLE `seccion_ee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `solicitud_practica`
--

DROP TABLE IF EXISTS `solicitud_practica`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `solicitud_practica` (
  `idSolicutud` int NOT NULL AUTO_INCREMENT,
  `nombreProyecto` varchar(45) NOT NULL,
  `razonSocialOrganizacion` varchar(45) NOT NULL,
  `Estudiante_idEstudiante` int NOT NULL,
  PRIMARY KEY (`idSolicutud`),
  KEY `fk_Solicitud_practica_Estudiante1_idx` (`Estudiante_idEstudiante`),
  CONSTRAINT `fk_Solicitud_practica_Estudiante1` FOREIGN KEY (`Estudiante_idEstudiante`) REFERENCES `estudiante` (`idEstudiante`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `solicitud_practica`
--

LOCK TABLES `solicitud_practica` WRITE;
/*!40000 ALTER TABLE `solicitud_practica` DISABLE KEYS */;
/*!40000 ALTER TABLE `solicitud_practica` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `idUsuario` int NOT NULL AUTO_INCREMENT,
  `correoInstitucional` varchar(45) NOT NULL,
  `contraseña` varchar(255) NOT NULL,
  `estado` int NOT NULL,
  `tipoUsuario` varchar(45) NOT NULL,
  PRIMARY KEY (`idUsuario`),
  KEY `fk_Usuario_Estado_Usuario1_idx` (`estado`),
  CONSTRAINT `fk_Usuario_Estado_Usuario1` FOREIGN KEY (`estado`) REFERENCES `estado_usuario` (`idEstadoUsuario`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,'zs24013292@estudiantes.uv.mx','$2a$12$9gpFaV11P10Xl35J4itI/e8EmY3pG5HTg0yM6XBjHUM7FEQf/Rkfq',1,'ESTUDIANTE'),(2,'juanperez@uv.mx','$2a$12$9gpFaV11P10Xl35J4itI/e8EmY3pG5HTg0yM6XBjHUM7FEQf/Rkfq',1,'PROFESOR'),(3,'marialopez@uv.mx','$2a$12$9gpFaV11P10Xl35J4itI/e8EmY3pG5HTg0yM6XBjHUM7FEQf/Rkfq',1,'COORDINADOR'),(4,'arturobaez@uv.mx','$2a$12$9gpFaV11P10Xl35J4itI/e8EmY3pG5HTg0yM6XBjHUM7FEQf/Rkfq',1,'ADMINISTRADOR'),(5,'profesor@uv.mx','$2a$12$9gpFaV11P10Xl35J4itI/e8EmY3pG5HTg0yM6XBjHUM7FEQf/Rkfq',1,'PROFESOR'),(6,'estudiante@uv.mx','$2a$12$9gpFaV11P10Xl35J4itI/e8EmY3pG5HTg0yM6XBjHUM7FEQf/Rkfq',1,'ESTUDIANTE');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-13  9:55:31
