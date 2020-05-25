-- phpMyAdmin SQL Dump
-- version 4.9.3
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost:8889
-- Généré le :  lun. 25 mai 2020 à 12:48
-- Version du serveur :  5.7.26
-- Version de PHP :  7.4.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Base de données :  `ocean-database`
--

-- --------------------------------------------------------

--
-- Structure de la table `client`
--

CREATE TABLE `client` (
  `userId` int(11) NOT NULL,
  `userName` varchar(255) NOT NULL,
  `userType` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `client`
--

INSERT INTO `client` (`userId`, `userName`, `userType`) VALUES
(0, 'User Demo', 'Test');

-- --------------------------------------------------------

--
-- Structure de la table `config`
--

CREATE TABLE `config` (
  `oceanBoxNumber` int(11) NOT NULL,
  `idClient` int(11) NOT NULL,
  `oceanBoxIP` varchar(255) NOT NULL DEFAULT '0.0.0.0',
  `videoStream` varchar(255) NOT NULL DEFAULT 'default',
  `wakingHour` varchar(255) NOT NULL DEFAULT '06:00:00',
  `activateStandby` tinyint(1) NOT NULL DEFAULT '1',
  `timeBeforeStandby` varchar(255) NOT NULL DEFAULT '00:30:00',
  `nextDownloadTime` varchar(255) NOT NULL DEFAULT 'UNKNOWN',
  `videoPath` varchar(255) NOT NULL DEFAULT '/home/pi/OceanBox/video/',
  `relativeLogPath` varchar(255) NOT NULL DEFAULT 'UNKNOWN'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `config`
--

INSERT INTO `config` (`oceanBoxNumber`, `idClient`, `oceanBoxIP`, `videoStream`, `wakingHour`, `activateStandby`, `timeBeforeStandby`, `nextDownloadTime`, `videoPath`, `relativeLogPath`) VALUES
(0, 0, '192.168.1.17', 'default', '08:00:00', 0, '00:00:20', 'Tue May 26 14:41:45 CEST 2020', '/home/pi/OceanBox/video/', 'allAboutFtp.log');

-- --------------------------------------------------------

--
-- Structure de la table `db`
--

CREATE TABLE `db` (
  `dbUser` varchar(255) NOT NULL,
  `dbIP` varchar(255) NOT NULL,
  `dbPassword` varchar(255) NOT NULL,
  `dbPort` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `db`
--

INSERT INTO `db` (`dbUser`, `dbIP`, `dbPassword`, `dbPort`) VALUES
('ocean_bdd', '37.187.107.122', 'OceanBox2020', 3306);

-- --------------------------------------------------------

--
-- Structure de la table `ftp`
--

CREATE TABLE `ftp` (
  `ftpUser` varchar(255) NOT NULL,
  `ftpIP` varchar(255) NOT NULL,
  `ftpPassword` varchar(255) NOT NULL,
  `ftpPort` int(11) NOT NULL,
  `ftpVideoPath` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `ftp`
--

INSERT INTO `ftp` (`ftpUser`, `ftpIP`, `ftpPassword`, `ftpPort`, `ftpVideoPath`) VALUES
('ocean_ftp', '37.187.107.122', 'Stream2020', 21, '/default_video/');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `client`
--
ALTER TABLE `client`
  ADD PRIMARY KEY (`userId`);

--
-- Index pour la table `config`
--
ALTER TABLE `config`
  ADD PRIMARY KEY (`oceanBoxNumber`),
  ADD KEY `idClient` (`idClient`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `client`
--
ALTER TABLE `client`
  MODIFY `userId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `config`
--
ALTER TABLE `config`
  MODIFY `oceanBoxNumber` int(11) NOT NULL AUTO_INCREMENT;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `config`
--
ALTER TABLE `config`
  ADD CONSTRAINT `config_ibfk_1` FOREIGN KEY (`idClient`) REFERENCES `client` (`userId`);
