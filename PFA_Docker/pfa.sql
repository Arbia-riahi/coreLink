-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : sam. 15 nov. 2025 à 19:39
-- Version du serveur : 10.4.28-MariaDB
-- Version de PHP : 8.0.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `pfa`
--

-- --------------------------------------------------------

--
-- Structure de la table `actualites`
--

CREATE TABLE `actualites` (
  `id` bigint(20) NOT NULL,
  `date_creation` datetime(6) NOT NULL,
  `description` text DEFAULT NULL,
  `titre` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `actualites`
--

INSERT INTO `actualites` (`id`, `date_creation`, `description`, `titre`) VALUES
(1, '2025-11-14 21:39:15.000000', 'Tempore at in id laborum assumenda deleniti illum deserunt.', 'La Discipline des orphelins#5fbca082-ff4e-4bd7-8372-060da82c27ef'),
(2, '2025-11-14 21:44:18.000000', 'Neque quo doloribus qui sunt iste corrupti voluptates id voluptates iusto.', 'LOdyssée invisible#e5a43dca-d29a-4dc7-bbf8-7a51f8ea98b0'),
(3, '2025-11-14 21:46:38.000000', 'Veritatis vel qui aliquam maiores veniam ullam repudiandae cumque ducimus voluptatibus labore perspiciatis id.', 'La Soumission comme passion#4d42939e-a89e-496a-b36c-a5f214821eb5'),
(4, '2025-11-14 21:48:58.000000', 'Id suscipit quia accusamus libero expedita dolorem provident quae voluptatem.', 'Pourquoi je mens ?#823bf3b8-bdc6-4ff9-980c-eb8e419a0abb'),
(5, '2025-11-14 21:50:50.000000', 'Ea quaerat necessitatibus nisi porro molestias ut deserunt praesentium non quaerat libero corporis.', 'La Peau des savants#02fa432d-e81e-4f78-8586-a504a3855879'),
(6, '2025-11-14 21:52:50.000000', 'Labore voluptate iure nesciunt facere et perspiciatis commodi expedita similique ipsum molestiae voluptatem id.', 'Le Désir des femmes fortes#35570103-c227-426f-b785-f0c11776443c'),
(8, '2025-11-15 13:23:15.000000', 'Tenetur architecto dolores est possimus rerum quod perspiciatis sed cumque odit sit recusandae nam.', 'LOdeur du sanglier#0063762e-d056-4b8b-9988-162f64dca828'),
(9, '2025-11-15 13:28:09.000000', 'Porro qui nulla impedit enim nesciunt soluta accusantium excepturi.', 'La Soumission comme passion#7ed60429-95f7-46f4-abb0-6629f7769fd8'),
(10, '2025-11-15 13:30:09.000000', 'Vel enim neque iste necessitatibus quia assumenda quia iste praesentium.', 'Le Siècle de la rue voisine#8b699dd6-abdf-45ff-b8d9-0ff046720fd5'),
(11, '2025-11-15 13:32:12.000000', 'Exercitationem architecto aut rerum quo ut est atque maxime.', 'La Soumission comme passion#0df3cb6b-2956-4f51-b172-1c6f5291706c'),
(12, '2025-11-15 13:34:06.000000', 'Culpa dignissimos consequatur iusto nisi aut nulla aut est et non ut.', 'La Peau des savants#5574e954-ab51-42d4-a571-d91da9d5c719'),
(13, '2025-11-15 13:35:58.000000', 'Et cum libero libero non amet atque asperiores reprehenderit.', 'Le Désir des femmes fortes#fa8ba79e-de85-4b2b-8066-de4e748dba94'),
(15, '2025-11-15 16:00:17.000000', 'At rem neque temporibus sit odio voluptatem ut quos eos architecto velit repellendus modi.', 'Le Désir des femmes fortes#b7472ca9-af2c-4ae1-953e-25118c681843'),
(16, '2025-11-15 16:04:35.000000', 'Vel sit numquam harum voluptatem repellat cum rem praesentium recusandae.', 'Le Désir des femmes fortes#637a1609-979b-42a9-a739-c5d3099d8e64'),
(17, '2025-11-15 16:06:32.000000', 'Commodi molestiae officia amet vel accusamus eum cum aut.', 'Le Désir des femmes fortes#aef807e7-5ba6-41b8-a438-ba0d8fecadff'),
(18, '2025-11-15 16:08:31.000000', 'Ipsa quia sed debitis adipisci aliquid architecto facere nihil non.', 'Le Désir des femmes fortes#aaf603d9-93fb-48ee-8b3a-844ff9e12668'),
(19, '2025-11-15 16:10:26.000000', 'Culpa dolores suscipit placeat sequi earum ad aperiam fuga.', 'Le Couloir de tous les mépris#2e2e7227-27d1-4fa2-8026-d48649d7b687'),
(20, '2025-11-15 16:12:21.000000', 'Doloremque esse dolor occaecati totam quo omnis culpa ea deserunt ducimus.', 'La progéniture du mal#efd7393e-db22-4ade-858a-b992e60968dc');

-- --------------------------------------------------------

--
-- Structure de la table `actualite_images`
--

CREATE TABLE `actualite_images` (
  `actualite_id` bigint(20) NOT NULL,
  `photo` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `actualite_images`
--

INSERT INTO `actualite_images` (`actualite_id`, `photo`) VALUES
(1, 'http://localhost:8080/uploads/files/NLSH0UK4LSNOKTO9XM_image 1.jpeg'),
(1, 'http://localhost:8080/uploads/files/H4ABWQV9EYNBII4AC6_image 2.jpeg'),
(1, 'http://localhost:8080/uploads/files/MGVUXBB6U6SYHN567Y_exception 1.jpg'),
(1, 'http://localhost:8080/uploads/files/QUVW96XFN36UERA6SG_exception 2.jpg'),
(1, 'http://localhost:8080/uploads/files/GUVK3VJYPAXWO40U93_exception 3.jpg'),
(2, 'http://localhost:8080/uploads/files/HHF5DGCO8FVT0FNTA4_image 1.jpeg'),
(2, 'http://localhost:8080/uploads/files/OX3PYZCJ8IGLFLW5WF_image 2.jpeg'),
(2, 'http://localhost:8080/uploads/files/18FB3ZIAPYX3FIZCT7_exception 1.jpg'),
(2, 'http://localhost:8080/uploads/files/UJ79TADI9F46I51E4I_exception 2.jpg'),
(3, 'http://localhost:8080/uploads/files/http://localhost:8080/uploads/files/9KTXRHDX9P2FYMZOBZ_image 1.jpeg'),
(3, 'http://localhost:8080/uploads/files/http://localhost:8080/uploads/files/7H81APNVE58RYE6L48_image 2.jpeg'),
(3, 'http://localhost:8080/uploads/files/http://localhost:8080/uploads/files/3JGKHSW9GTENDLR71Q_exception 1.jpg'),
(3, 'http://localhost:8080/uploads/files/http://localhost:8080/uploads/files/93Y639J19YKTMXHH9C_exception 2.jpg'),
(3, 'http://localhost:8080/uploads/files/http://localhost:8080/uploads/files/7MACD4K4083JVV9MC3_exception 3.jpg'),
(4, 'http://localhost:8080/uploads/files/FOF9Z69O7AYDTGZ9TO_image 1.jpeg'),
(4, 'http://localhost:8080/uploads/files/PSNMRYRN6J8K296KCJ_image 2.jpeg'),
(4, 'http://localhost:8080/uploads/files/VERD07UTPEYVWR2EAF_exception 1.jpg'),
(4, 'http://localhost:8080/uploads/files/MK4M9YSCX28H7SE5UW_exception 2.jpg'),
(4, 'http://localhost:8080/uploads/files/SP9HVFREMF1B18SPRN_exception 3.jpg'),
(5, 'http://localhost:8080/uploads/files/B0EP2CW33K4MKNTV0G_image 1.jpeg'),
(5, 'http://localhost:8080/uploads/files/RI3ZWLYP91FYL00JHH_image 2.jpeg'),
(5, 'http://localhost:8080/uploads/files/A9MUQ1QI0K5AVLMZLG_exception 1.jpg'),
(5, 'http://localhost:8080/uploads/files/GTL0K7OJ58SBXMUTQY_exception 2.jpg'),
(5, 'http://localhost:8080/uploads/files/5I5TZ3DA7DS3GVMWEO_exception 3.jpg'),
(6, 'http://localhost:8080/uploads/files/7D7KSZBNH5WCSAC2JK_image 1.jpeg'),
(6, 'http://localhost:8080/uploads/files/LWRPWRPUO0E30IZRT6_image 2.jpeg'),
(6, 'http://localhost:8080/uploads/files/D4N0KQ7FS51MGB1ER8_exception 1.jpg'),
(6, 'http://localhost:8080/uploads/files/RIBZVUZJOVALIME7K7_exception 2.jpg'),
(6, 'http://localhost:8080/uploads/files/592S7YIUIZJG7LRX9B_exception 3.jpg'),
(8, 'http://localhost:8080/uploads/files/P4ZKFMH7RO3NTKIGLN_image 1.jpeg'),
(8, 'http://localhost:8080/uploads/files/4JC203VHBBOJG6Q4RW_image 2.jpeg'),
(8, 'http://localhost:8080/uploads/files/BC7DERM89IA1MNHNC1_exception 1.jpg'),
(8, 'http://localhost:8080/uploads/files/8GYVWWW1IFMFZTF0JG_exception 2.jpg'),
(8, 'http://localhost:8080/uploads/files/TBZQQ45XK2HQEY950W_exception 3.jpg'),
(9, 'http://localhost:8080/uploads/files/RCXY1HFDKXBC1L8IYA_image 1.jpeg'),
(9, 'http://localhost:8080/uploads/files/MQ3FRM98DOAO8KIF95_image 2.jpeg'),
(9, 'http://localhost:8080/uploads/files/HTQE0CJ30S2E090EWO_exception 1.jpg'),
(9, 'http://localhost:8080/uploads/files/UF6MWSNF1Y418W5ER4_exception 2.jpg'),
(10, 'http://localhost:8080/uploads/files/http://localhost:8080/uploads/files/SAK8L6G7LLD9HX6YPQ_image 1.jpeg'),
(10, 'http://localhost:8080/uploads/files/http://localhost:8080/uploads/files/UCOHK7BRRU4U2IZWNM_image 2.jpeg'),
(10, 'http://localhost:8080/uploads/files/http://localhost:8080/uploads/files/H6EX7389FZYM0XJJN4_exception 1.jpg'),
(10, 'http://localhost:8080/uploads/files/http://localhost:8080/uploads/files/G0RL76QDPDE0K9FEDK_exception 2.jpg'),
(10, 'http://localhost:8080/uploads/files/http://localhost:8080/uploads/files/QXND3RLRXPL3SOD6DR_exception 3.jpg'),
(11, 'http://localhost:8080/uploads/files/Z8YICD912TK508ENA6_image 1.jpeg'),
(11, 'http://localhost:8080/uploads/files/HRTKHTXPM8O1R0U956_image 2.jpeg'),
(11, 'http://localhost:8080/uploads/files/931GZEVJLR2M8U849M_exception 1.jpg'),
(11, 'http://localhost:8080/uploads/files/4HYQGLCRFFXLEHYL55_exception 2.jpg'),
(11, 'http://localhost:8080/uploads/files/CSTDUFG8JXKIL08QDG_exception 3.jpg'),
(12, 'http://localhost:8080/uploads/files/7K99HSWUJ75UV30GBJ_image 1.jpeg'),
(12, 'http://localhost:8080/uploads/files/GPNUQ4RSGJJTCKIVYB_image 2.jpeg'),
(12, 'http://localhost:8080/uploads/files/4RZCSAG3MST3BCBV77_exception 1.jpg'),
(12, 'http://localhost:8080/uploads/files/B61TK1OXYK9AFBVKSL_exception 2.jpg'),
(12, 'http://localhost:8080/uploads/files/D2K7JNEJ9QPUZQWVX3_exception 3.jpg'),
(13, 'http://localhost:8080/uploads/files/YZFVC6VM5Z7QMAU758_image 1.jpeg'),
(13, 'http://localhost:8080/uploads/files/7CXADII2R2RVSHAU69_image 2.jpeg'),
(13, 'http://localhost:8080/uploads/files/M68WA57ALXAJAGBWHG_exception 1.jpg'),
(13, 'http://localhost:8080/uploads/files/YE515NXOJL11DGCN4W_exception 2.jpg'),
(13, 'http://localhost:8080/uploads/files/LMYFYMQS2F60C0D0ZY_exception 3.jpg'),
(15, 'http://localhost:8080/uploads/files/ZC81U12PMU83Q46WPJ_image 1.jpeg'),
(15, 'http://localhost:8080/uploads/files/M23FV38VQ4D8Q9YZH9_image 2.jpeg'),
(15, 'http://localhost:8080/uploads/files/UAA3M8GIAAFRII6VNW_exception 1.jpg'),
(15, 'http://localhost:8080/uploads/files/DL98IB6BYTTP1XUBOZ_exception 2.jpg'),
(15, 'http://localhost:8080/uploads/files/2REZ7UE42WY0J5R38N_exception 3.jpg'),
(16, 'http://localhost:8080/uploads/files/AIXTAN1GNKQRW1MSI1_image 1.jpeg'),
(16, 'http://localhost:8080/uploads/files/RHMQOD52PWJJ88H17S_image 2.jpeg'),
(16, 'http://localhost:8080/uploads/files/ZU3XTRGM3S0LQH017W_exception 1.jpg'),
(16, 'http://localhost:8080/uploads/files/2SP9BSQL1931290D41_exception 2.jpg'),
(17, 'http://localhost:8080/uploads/files/http://localhost:8080/uploads/files/P7MRO6Q8JYS9FHRL7O_image 1.jpeg'),
(17, 'http://localhost:8080/uploads/files/http://localhost:8080/uploads/files/R0Q4CAMP4JBFMV70EG_image 2.jpeg'),
(17, 'http://localhost:8080/uploads/files/http://localhost:8080/uploads/files/5HP2B1GPZF6MPZ2LNP_exception 1.jpg'),
(17, 'http://localhost:8080/uploads/files/http://localhost:8080/uploads/files/QEGUSIUIAARO0OHQAN_exception 2.jpg'),
(17, 'http://localhost:8080/uploads/files/http://localhost:8080/uploads/files/U3YK3CEY0PI1DR60O4_exception 3.jpg'),
(18, 'http://localhost:8080/uploads/files/1HNB6H3X5ZB5DA9Y7T_image 1.jpeg'),
(18, 'http://localhost:8080/uploads/files/H3S270RFAGGXLR4UA9_image 2.jpeg'),
(18, 'http://localhost:8080/uploads/files/M5FSNLM9M57LAVLIHV_exception 1.jpg'),
(18, 'http://localhost:8080/uploads/files/TK3Y1HRNBF6Z91AF3I_exception 2.jpg'),
(18, 'http://localhost:8080/uploads/files/RZV5AOHAFTELHEW6JN_exception 3.jpg'),
(19, 'http://localhost:8080/uploads/files/4QORN8WW60HNGUSHIN_image 1.jpeg'),
(19, 'http://localhost:8080/uploads/files/YZSI7XM3XMSAF9OLUG_image 2.jpeg'),
(19, 'http://localhost:8080/uploads/files/X2877MWSI6RHIHR0QJ_exception 1.jpg'),
(19, 'http://localhost:8080/uploads/files/BZMYVUSMZ9GG58DX9T_exception 2.jpg'),
(19, 'http://localhost:8080/uploads/files/PRQK4NQ325JY4XXXWE_exception 3.jpg'),
(20, 'http://localhost:8080/uploads/files/F84GE8BM9SPLORS23O_image 1.jpeg'),
(20, 'http://localhost:8080/uploads/files/W5GQKNFG3NNA2T8PHG_image 2.jpeg'),
(20, 'http://localhost:8080/uploads/files/Z9U8AX6W8NX3J3R2SR_exception 1.jpg'),
(20, 'http://localhost:8080/uploads/files/L3B9JKMFRI3CYGY7XD_exception 2.jpg'),
(20, 'http://localhost:8080/uploads/files/UET9G9SQE7Y2W0LXNC_exception 3.jpg');

-- --------------------------------------------------------

--
-- Structure de la table `commentaires`
--

CREATE TABLE `commentaires` (
  `id` bigint(20) NOT NULL,
  `contenu` text DEFAULT NULL,
  `date` datetime(6) NOT NULL,
  `modifie` bit(1) NOT NULL,
  `supprime` bit(1) NOT NULL,
  `auteur_matricule` varchar(20) DEFAULT NULL,
  `parent_comment_id` bigint(20) DEFAULT NULL,
  `tache_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `commentaire_mentions`
--

CREATE TABLE `commentaire_mentions` (
  `commentaire_id` bigint(20) NOT NULL,
  `user_matricule` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `messages`
--

CREATE TABLE `messages` (
  `id` bigint(20) NOT NULL,
  `audio_url` varchar(255) DEFAULT NULL,
  `content` text DEFAULT NULL,
  `file_url` varchar(255) DEFAULT NULL,
  `timestamp` datetime(6) NOT NULL,
  `sender_id` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `message_mentions`
--

CREATE TABLE `message_mentions` (
  `message_id` bigint(20) NOT NULL,
  `user_id` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `message_seen_by`
--

CREATE TABLE `message_seen_by` (
  `message_id` bigint(20) NOT NULL,
  `user_id` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `notifications`
--

CREATE TABLE `notifications` (
  `id` bigint(20) NOT NULL,
  `date` datetime(6) NOT NULL,
  `lue` bit(1) NOT NULL,
  `message` varchar(255) NOT NULL,
  `type` enum('PROJET_DEADLINE_PROCHE','PROJET_DEADLINE_DEPASSEE','AFFECTATION_MEMBRE_PROJET','TACHE_ASSIGNATION','TACHE_DEADLINE_PROCHE','TACHE_DEADLINE_DEPASSEE','NOUVEAU_COMMENTAIRE','REPONSE_COMMENTAIRE','MENTION_DANS_COMMENTAIRE') NOT NULL,
  `destinataire_matricule` varchar(20) DEFAULT NULL,
  `projet_lie_numero_projet` bigint(20) DEFAULT NULL,
  `tache_liee_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `notifications`
--

INSERT INTO `notifications` (`id`, `date`, `lue`, `message`, `type`, `destinataire_matricule`, `projet_lie_numero_projet`, `tache_liee_id`) VALUES
(1, '2025-11-14 20:33:18.000000', b'0', 'Vous avez été ajouté au projet : Project Management', 'AFFECTATION_MEMBRE_PROJET', 'wafa', 1, NULL),
(2, '2025-11-15 10:32:13.000000', b'0', 'Vous avez été ajouté au projet : La Promise du voyeur#fb68c983-30fd-492c-9c69-27aa0493ba7f', 'AFFECTATION_MEMBRE_PROJET', 'salma', 2, NULL),
(3, '2025-11-15 10:32:13.000000', b'0', 'Vous avez été ajouté au projet : La Promise du voyeur#fb68c983-30fd-492c-9c69-27aa0493ba7f', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 2, NULL),
(4, '2025-11-15 10:32:13.000000', b'0', 'Vous avez été ajouté au projet : La Promise du voyeur#fb68c983-30fd-492c-9c69-27aa0493ba7f', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 2, NULL),
(5, '2025-11-15 10:32:13.000000', b'0', 'Vous avez été ajouté au projet : La Promise du voyeur#fb68c983-30fd-492c-9c69-27aa0493ba7f', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 2, NULL),
(6, '2025-11-15 10:32:13.000000', b'0', 'Vous avez été ajouté au projet : La Promise du voyeur#fb68c983-30fd-492c-9c69-27aa0493ba7f', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 2, NULL),
(7, '2025-11-15 10:55:48.000000', b'0', 'Vous avez été ajouté au projet : La Promise du voyeur#67e2a35b-ee95-4122-a653-32c77f561f1c', 'AFFECTATION_MEMBRE_PROJET', 'salma', 3, NULL),
(8, '2025-11-15 10:55:48.000000', b'0', 'Vous avez été ajouté au projet : La Promise du voyeur#67e2a35b-ee95-4122-a653-32c77f561f1c', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 3, NULL),
(9, '2025-11-15 10:55:48.000000', b'0', 'Vous avez été ajouté au projet : La Promise du voyeur#67e2a35b-ee95-4122-a653-32c77f561f1c', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 3, NULL),
(10, '2025-11-15 10:55:48.000000', b'0', 'Vous avez été ajouté au projet : La Promise du voyeur#67e2a35b-ee95-4122-a653-32c77f561f1c', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 3, NULL),
(11, '2025-11-15 10:55:48.000000', b'0', 'Vous avez été ajouté au projet : La Promise du voyeur#67e2a35b-ee95-4122-a653-32c77f561f1c', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 3, NULL),
(12, '2025-11-15 11:09:07.000000', b'0', 'Vous avez été ajouté au projet : La Soumission comme passion#2aa841bc-d617-459b-802c-e84e3a155e3d', 'AFFECTATION_MEMBRE_PROJET', 'salma', 4, NULL),
(13, '2025-11-15 11:09:07.000000', b'0', 'Vous avez été ajouté au projet : La Soumission comme passion#2aa841bc-d617-459b-802c-e84e3a155e3d', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 4, NULL),
(14, '2025-11-15 11:09:07.000000', b'0', 'Vous avez été ajouté au projet : La Soumission comme passion#2aa841bc-d617-459b-802c-e84e3a155e3d', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 4, NULL),
(15, '2025-11-15 11:09:07.000000', b'0', 'Vous avez été ajouté au projet : La Soumission comme passion#2aa841bc-d617-459b-802c-e84e3a155e3d', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 4, NULL),
(16, '2025-11-15 11:09:07.000000', b'0', 'Vous avez été ajouté au projet : La Soumission comme passion#2aa841bc-d617-459b-802c-e84e3a155e3d', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 4, NULL),
(17, '2025-11-15 11:11:27.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#22a32114-6558-40ec-af5c-a4e74ca8d2f6', 'AFFECTATION_MEMBRE_PROJET', 'salma', 5, NULL),
(18, '2025-11-15 11:11:27.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#22a32114-6558-40ec-af5c-a4e74ca8d2f6', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 5, NULL),
(19, '2025-11-15 11:11:27.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#22a32114-6558-40ec-af5c-a4e74ca8d2f6', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 5, NULL),
(20, '2025-11-15 11:11:27.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#22a32114-6558-40ec-af5c-a4e74ca8d2f6', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 5, NULL),
(21, '2025-11-15 11:11:27.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#22a32114-6558-40ec-af5c-a4e74ca8d2f6', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 5, NULL),
(22, '2025-11-15 11:13:53.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#c61438be-11e3-4082-ba64-35de37cec5b6', 'AFFECTATION_MEMBRE_PROJET', 'salma', 6, NULL),
(23, '2025-11-15 11:13:53.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#c61438be-11e3-4082-ba64-35de37cec5b6', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 6, NULL),
(24, '2025-11-15 11:13:53.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#c61438be-11e3-4082-ba64-35de37cec5b6', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 6, NULL),
(25, '2025-11-15 11:13:53.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#c61438be-11e3-4082-ba64-35de37cec5b6', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 6, NULL),
(26, '2025-11-15 11:13:53.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#c61438be-11e3-4082-ba64-35de37cec5b6', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 6, NULL),
(27, '2025-11-15 11:17:46.000000', b'0', 'Vous avez été ajouté au projet : LOdyssée invisible#3d591cd9-5aa4-43de-96ed-5c3195b8bab6', 'AFFECTATION_MEMBRE_PROJET', 'salma', 7, NULL),
(28, '2025-11-15 11:17:46.000000', b'0', 'Vous avez été ajouté au projet : LOdyssée invisible#3d591cd9-5aa4-43de-96ed-5c3195b8bab6', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 7, NULL),
(29, '2025-11-15 11:17:46.000000', b'0', 'Vous avez été ajouté au projet : LOdyssée invisible#3d591cd9-5aa4-43de-96ed-5c3195b8bab6', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 7, NULL),
(30, '2025-11-15 11:17:46.000000', b'0', 'Vous avez été ajouté au projet : LOdyssée invisible#3d591cd9-5aa4-43de-96ed-5c3195b8bab6', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 7, NULL),
(31, '2025-11-15 11:17:46.000000', b'0', 'Vous avez été ajouté au projet : LOdyssée invisible#3d591cd9-5aa4-43de-96ed-5c3195b8bab6', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 7, NULL),
(32, '2025-11-15 11:39:57.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#68f328f9-3a8c-4edc-9d80-eb8ad23051b9', 'AFFECTATION_MEMBRE_PROJET', 'salma', 8, NULL),
(33, '2025-11-15 11:39:57.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#68f328f9-3a8c-4edc-9d80-eb8ad23051b9', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 8, NULL),
(34, '2025-11-15 11:39:57.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#68f328f9-3a8c-4edc-9d80-eb8ad23051b9', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 8, NULL),
(35, '2025-11-15 11:39:57.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#68f328f9-3a8c-4edc-9d80-eb8ad23051b9', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 8, NULL),
(36, '2025-11-15 11:39:57.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#68f328f9-3a8c-4edc-9d80-eb8ad23051b9', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 8, NULL),
(37, '2025-11-15 11:48:28.000000', b'0', 'Vous avez été ajouté au projet : Pourquoi je mens ?#e2e0f3df-04a7-49f6-828f-81c349f23614', 'AFFECTATION_MEMBRE_PROJET', 'salma', 9, NULL),
(38, '2025-11-15 11:48:28.000000', b'0', 'Vous avez été ajouté au projet : Pourquoi je mens ?#e2e0f3df-04a7-49f6-828f-81c349f23614', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 9, NULL),
(39, '2025-11-15 11:48:28.000000', b'0', 'Vous avez été ajouté au projet : Pourquoi je mens ?#e2e0f3df-04a7-49f6-828f-81c349f23614', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 9, NULL),
(40, '2025-11-15 11:48:28.000000', b'0', 'Vous avez été ajouté au projet : Pourquoi je mens ?#e2e0f3df-04a7-49f6-828f-81c349f23614', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 9, NULL),
(41, '2025-11-15 11:48:28.000000', b'0', 'Vous avez été ajouté au projet : Pourquoi je mens ?#e2e0f3df-04a7-49f6-828f-81c349f23614', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 9, NULL),
(42, '2025-11-15 11:59:20.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#57eeecd5-80d2-453b-8732-3c385694c101', 'AFFECTATION_MEMBRE_PROJET', 'salma', 10, NULL),
(43, '2025-11-15 11:59:20.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#57eeecd5-80d2-453b-8732-3c385694c101', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 10, NULL),
(44, '2025-11-15 11:59:20.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#57eeecd5-80d2-453b-8732-3c385694c101', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 10, NULL),
(45, '2025-11-15 11:59:20.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#57eeecd5-80d2-453b-8732-3c385694c101', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 10, NULL),
(46, '2025-11-15 11:59:20.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#57eeecd5-80d2-453b-8732-3c385694c101', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 10, NULL),
(47, '2025-11-15 12:11:59.000000', b'0', 'Vous avez été ajouté au projet : La Soumission comme passion#a86d0cdf-08a9-4d79-9d10-840bd0a46985', 'AFFECTATION_MEMBRE_PROJET', 'salma', 11, NULL),
(48, '2025-11-15 12:11:59.000000', b'0', 'Vous avez été ajouté au projet : La Soumission comme passion#a86d0cdf-08a9-4d79-9d10-840bd0a46985', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 11, NULL),
(49, '2025-11-15 12:11:59.000000', b'0', 'Vous avez été ajouté au projet : La Soumission comme passion#a86d0cdf-08a9-4d79-9d10-840bd0a46985', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 11, NULL),
(50, '2025-11-15 12:11:59.000000', b'0', 'Vous avez été ajouté au projet : La Soumission comme passion#a86d0cdf-08a9-4d79-9d10-840bd0a46985', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 11, NULL),
(51, '2025-11-15 12:11:59.000000', b'0', 'Vous avez été ajouté au projet : La Soumission comme passion#a86d0cdf-08a9-4d79-9d10-840bd0a46985', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 11, NULL),
(52, '2025-11-15 12:12:56.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#e18ec697-50a0-4ccb-8fbf-5ffcfbad35d0', 'AFFECTATION_MEMBRE_PROJET', 'salma', 12, NULL),
(53, '2025-11-15 12:12:56.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#e18ec697-50a0-4ccb-8fbf-5ffcfbad35d0', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 12, NULL),
(54, '2025-11-15 12:12:56.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#e18ec697-50a0-4ccb-8fbf-5ffcfbad35d0', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 12, NULL),
(55, '2025-11-15 12:12:56.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#e18ec697-50a0-4ccb-8fbf-5ffcfbad35d0', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 12, NULL),
(56, '2025-11-15 12:12:56.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#e18ec697-50a0-4ccb-8fbf-5ffcfbad35d0', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 12, NULL),
(57, '2025-11-15 12:14:15.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#a461681d-7b71-4f7c-9577-ef0a2847f170', 'AFFECTATION_MEMBRE_PROJET', 'salma', 13, NULL),
(58, '2025-11-15 12:14:15.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#a461681d-7b71-4f7c-9577-ef0a2847f170', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 13, NULL),
(59, '2025-11-15 12:14:15.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#a461681d-7b71-4f7c-9577-ef0a2847f170', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 13, NULL),
(60, '2025-11-15 12:14:15.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#a461681d-7b71-4f7c-9577-ef0a2847f170', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 13, NULL),
(61, '2025-11-15 12:14:15.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#a461681d-7b71-4f7c-9577-ef0a2847f170', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 13, NULL),
(62, '2025-11-15 12:15:16.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#5b836bd5-a589-4af4-b19d-56f028c3998d', 'AFFECTATION_MEMBRE_PROJET', 'salma', 14, NULL),
(63, '2025-11-15 12:15:16.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#5b836bd5-a589-4af4-b19d-56f028c3998d', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 14, NULL),
(64, '2025-11-15 12:15:16.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#5b836bd5-a589-4af4-b19d-56f028c3998d', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 14, NULL),
(65, '2025-11-15 12:15:16.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#5b836bd5-a589-4af4-b19d-56f028c3998d', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 14, NULL),
(66, '2025-11-15 12:15:16.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#5b836bd5-a589-4af4-b19d-56f028c3998d', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 14, NULL),
(67, '2025-11-15 12:16:22.000000', b'0', 'Vous avez été ajouté au projet : La Soumission comme passion#930cab62-a60e-472f-b061-8d99bdb7f1d8', 'AFFECTATION_MEMBRE_PROJET', 'salma', 15, NULL),
(68, '2025-11-15 12:16:22.000000', b'0', 'Vous avez été ajouté au projet : La Soumission comme passion#930cab62-a60e-472f-b061-8d99bdb7f1d8', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 15, NULL),
(69, '2025-11-15 12:16:22.000000', b'0', 'Vous avez été ajouté au projet : La Soumission comme passion#930cab62-a60e-472f-b061-8d99bdb7f1d8', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 15, NULL),
(70, '2025-11-15 12:16:22.000000', b'0', 'Vous avez été ajouté au projet : La Soumission comme passion#930cab62-a60e-472f-b061-8d99bdb7f1d8', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 15, NULL),
(71, '2025-11-15 12:16:22.000000', b'0', 'Vous avez été ajouté au projet : La Soumission comme passion#930cab62-a60e-472f-b061-8d99bdb7f1d8', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 15, NULL),
(72, '2025-11-15 12:17:20.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#daf4e655-65e9-4285-909b-420f7ea19e6b', 'AFFECTATION_MEMBRE_PROJET', 'salma', 16, NULL),
(73, '2025-11-15 12:17:20.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#daf4e655-65e9-4285-909b-420f7ea19e6b', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 16, NULL),
(74, '2025-11-15 12:17:20.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#daf4e655-65e9-4285-909b-420f7ea19e6b', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 16, NULL),
(75, '2025-11-15 12:17:20.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#daf4e655-65e9-4285-909b-420f7ea19e6b', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 16, NULL),
(76, '2025-11-15 12:17:20.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#daf4e655-65e9-4285-909b-420f7ea19e6b', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 16, NULL),
(77, '2025-11-15 12:18:15.000000', b'0', 'Vous avez été ajouté au projet : LOdyssée invisible#85ebc8e0-eacb-4ec7-a3cc-93806a7c4b6b', 'AFFECTATION_MEMBRE_PROJET', 'salma', 17, NULL),
(78, '2025-11-15 12:18:15.000000', b'0', 'Vous avez été ajouté au projet : LOdyssée invisible#85ebc8e0-eacb-4ec7-a3cc-93806a7c4b6b', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 17, NULL),
(79, '2025-11-15 12:18:15.000000', b'0', 'Vous avez été ajouté au projet : LOdyssée invisible#85ebc8e0-eacb-4ec7-a3cc-93806a7c4b6b', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 17, NULL),
(80, '2025-11-15 12:18:15.000000', b'0', 'Vous avez été ajouté au projet : LOdyssée invisible#85ebc8e0-eacb-4ec7-a3cc-93806a7c4b6b', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 17, NULL),
(81, '2025-11-15 12:18:15.000000', b'0', 'Vous avez été ajouté au projet : LOdyssée invisible#85ebc8e0-eacb-4ec7-a3cc-93806a7c4b6b', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 17, NULL),
(82, '2025-11-15 12:19:11.000000', b'0', 'Vous avez été ajouté au projet : La Soumission comme passion#897f852e-dac7-4e1e-9f9b-9d3d7236fc91', 'AFFECTATION_MEMBRE_PROJET', 'salma', 18, NULL),
(83, '2025-11-15 12:19:11.000000', b'0', 'Vous avez été ajouté au projet : La Soumission comme passion#897f852e-dac7-4e1e-9f9b-9d3d7236fc91', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 18, NULL),
(84, '2025-11-15 12:19:11.000000', b'0', 'Vous avez été ajouté au projet : La Soumission comme passion#897f852e-dac7-4e1e-9f9b-9d3d7236fc91', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 18, NULL),
(85, '2025-11-15 12:19:11.000000', b'0', 'Vous avez été ajouté au projet : La Soumission comme passion#897f852e-dac7-4e1e-9f9b-9d3d7236fc91', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 18, NULL),
(86, '2025-11-15 12:19:11.000000', b'0', 'Vous avez été ajouté au projet : La Soumission comme passion#897f852e-dac7-4e1e-9f9b-9d3d7236fc91', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 18, NULL),
(87, '2025-11-15 12:19:55.000000', b'0', 'Vous avez été ajouté au projet : La Promise du voyeur#6084fbde-3b26-4491-8f34-bbc615681100', 'AFFECTATION_MEMBRE_PROJET', 'salma', 19, NULL),
(88, '2025-11-15 12:19:55.000000', b'0', 'Vous avez été ajouté au projet : La Promise du voyeur#6084fbde-3b26-4491-8f34-bbc615681100', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 19, NULL),
(89, '2025-11-15 12:19:55.000000', b'0', 'Vous avez été ajouté au projet : La Promise du voyeur#6084fbde-3b26-4491-8f34-bbc615681100', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 19, NULL),
(90, '2025-11-15 12:19:55.000000', b'0', 'Vous avez été ajouté au projet : La Promise du voyeur#6084fbde-3b26-4491-8f34-bbc615681100', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 19, NULL),
(91, '2025-11-15 12:19:55.000000', b'0', 'Vous avez été ajouté au projet : La Promise du voyeur#6084fbde-3b26-4491-8f34-bbc615681100', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 19, NULL),
(92, '2025-11-15 12:20:37.000000', b'0', 'Vous avez été ajouté au projet : La Peau des savants#6534644a-4cb5-453a-abab-78bb4976fd35', 'AFFECTATION_MEMBRE_PROJET', 'salma', 20, NULL),
(93, '2025-11-15 12:20:37.000000', b'0', 'Vous avez été ajouté au projet : La Peau des savants#6534644a-4cb5-453a-abab-78bb4976fd35', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 20, NULL),
(94, '2025-11-15 12:20:37.000000', b'0', 'Vous avez été ajouté au projet : La Peau des savants#6534644a-4cb5-453a-abab-78bb4976fd35', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 20, NULL),
(95, '2025-11-15 12:20:37.000000', b'0', 'Vous avez été ajouté au projet : La Peau des savants#6534644a-4cb5-453a-abab-78bb4976fd35', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 20, NULL),
(96, '2025-11-15 12:20:37.000000', b'0', 'Vous avez été ajouté au projet : La Peau des savants#6534644a-4cb5-453a-abab-78bb4976fd35', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 20, NULL),
(97, '2025-11-15 12:21:20.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#523ced5b-073f-4563-98f3-5946ff15a8f8', 'AFFECTATION_MEMBRE_PROJET', 'salma', 21, NULL),
(98, '2025-11-15 12:21:20.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#523ced5b-073f-4563-98f3-5946ff15a8f8', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 21, NULL),
(99, '2025-11-15 12:21:20.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#523ced5b-073f-4563-98f3-5946ff15a8f8', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 21, NULL),
(100, '2025-11-15 12:21:20.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#523ced5b-073f-4563-98f3-5946ff15a8f8', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 21, NULL),
(101, '2025-11-15 12:21:20.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#523ced5b-073f-4563-98f3-5946ff15a8f8', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 21, NULL),
(102, '2025-11-15 12:26:23.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#acf0ee42-4447-46f7-8e40-14db3e623a76', 'AFFECTATION_MEMBRE_PROJET', 'salma', 22, NULL),
(103, '2025-11-15 12:26:23.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#acf0ee42-4447-46f7-8e40-14db3e623a76', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 22, NULL),
(104, '2025-11-15 12:26:23.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#acf0ee42-4447-46f7-8e40-14db3e623a76', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 22, NULL),
(105, '2025-11-15 12:26:23.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#acf0ee42-4447-46f7-8e40-14db3e623a76', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 22, NULL),
(106, '2025-11-15 12:26:23.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#acf0ee42-4447-46f7-8e40-14db3e623a76', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 22, NULL),
(107, '2025-11-15 12:27:30.000000', b'0', 'Vous avez été ajouté au projet : La Peau des savants#b507d4c4-b593-4ef7-a927-d69579a83f40', 'AFFECTATION_MEMBRE_PROJET', 'salma', 23, NULL),
(108, '2025-11-15 12:27:30.000000', b'0', 'Vous avez été ajouté au projet : La Peau des savants#b507d4c4-b593-4ef7-a927-d69579a83f40', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 23, NULL),
(109, '2025-11-15 12:27:30.000000', b'0', 'Vous avez été ajouté au projet : La Peau des savants#b507d4c4-b593-4ef7-a927-d69579a83f40', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 23, NULL),
(110, '2025-11-15 12:27:30.000000', b'0', 'Vous avez été ajouté au projet : La Peau des savants#b507d4c4-b593-4ef7-a927-d69579a83f40', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 23, NULL),
(111, '2025-11-15 12:27:30.000000', b'0', 'Vous avez été ajouté au projet : La Peau des savants#b507d4c4-b593-4ef7-a927-d69579a83f40', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 23, NULL),
(112, '2025-11-15 12:30:49.000000', b'0', 'Vous avez été ajouté au projet : La Promise du voyeur#8e8c92af-c8e5-4432-900e-c2c96fba2d59', 'AFFECTATION_MEMBRE_PROJET', 'salma', 24, NULL),
(113, '2025-11-15 12:30:49.000000', b'0', 'Vous avez été ajouté au projet : La Promise du voyeur#8e8c92af-c8e5-4432-900e-c2c96fba2d59', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 24, NULL),
(114, '2025-11-15 12:30:49.000000', b'0', 'Vous avez été ajouté au projet : La Promise du voyeur#8e8c92af-c8e5-4432-900e-c2c96fba2d59', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 24, NULL),
(115, '2025-11-15 12:30:49.000000', b'0', 'Vous avez été ajouté au projet : La Promise du voyeur#8e8c92af-c8e5-4432-900e-c2c96fba2d59', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 24, NULL),
(116, '2025-11-15 12:30:49.000000', b'0', 'Vous avez été ajouté au projet : La Promise du voyeur#8e8c92af-c8e5-4432-900e-c2c96fba2d59', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 24, NULL),
(117, '2025-11-15 12:39:02.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#ef12b520-f05b-45d1-85ce-daa9ce789cb5', 'AFFECTATION_MEMBRE_PROJET', 'salma', 25, NULL),
(118, '2025-11-15 12:39:02.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#ef12b520-f05b-45d1-85ce-daa9ce789cb5', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 25, NULL),
(119, '2025-11-15 12:39:03.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#ef12b520-f05b-45d1-85ce-daa9ce789cb5', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 25, NULL),
(120, '2025-11-15 12:39:03.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#ef12b520-f05b-45d1-85ce-daa9ce789cb5', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 25, NULL),
(121, '2025-11-15 12:39:03.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#ef12b520-f05b-45d1-85ce-daa9ce789cb5', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 25, NULL),
(122, '2025-11-15 12:41:51.000000', b'0', 'Vous avez été ajouté au projet : La Promise du voyeur#3b7d568b-ac70-42c6-a079-0f347eaf0ef9', 'AFFECTATION_MEMBRE_PROJET', 'salma', 26, NULL),
(123, '2025-11-15 12:41:51.000000', b'0', 'Vous avez été ajouté au projet : La Promise du voyeur#3b7d568b-ac70-42c6-a079-0f347eaf0ef9', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 26, NULL),
(124, '2025-11-15 12:41:51.000000', b'0', 'Vous avez été ajouté au projet : La Promise du voyeur#3b7d568b-ac70-42c6-a079-0f347eaf0ef9', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 26, NULL),
(125, '2025-11-15 12:41:51.000000', b'0', 'Vous avez été ajouté au projet : La Promise du voyeur#3b7d568b-ac70-42c6-a079-0f347eaf0ef9', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 26, NULL),
(126, '2025-11-15 12:41:51.000000', b'0', 'Vous avez été ajouté au projet : La Promise du voyeur#3b7d568b-ac70-42c6-a079-0f347eaf0ef9', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 26, NULL),
(127, '2025-11-15 12:43:37.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#d01472ee-bcd1-45a2-b85c-cace8cb88dce', 'AFFECTATION_MEMBRE_PROJET', 'salma', 27, NULL),
(128, '2025-11-15 12:43:37.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#d01472ee-bcd1-45a2-b85c-cace8cb88dce', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 27, NULL),
(129, '2025-11-15 12:43:37.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#d01472ee-bcd1-45a2-b85c-cace8cb88dce', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 27, NULL),
(130, '2025-11-15 12:43:37.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#d01472ee-bcd1-45a2-b85c-cace8cb88dce', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 27, NULL),
(131, '2025-11-15 12:43:37.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#d01472ee-bcd1-45a2-b85c-cace8cb88dce', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 27, NULL),
(132, '2025-11-15 12:44:47.000000', b'0', 'Vous avez été ajouté au projet : La Soumission comme passion#3b6aba6e-d658-44e4-a047-abf025bbd6c9', 'AFFECTATION_MEMBRE_PROJET', 'salma', 28, NULL),
(133, '2025-11-15 12:44:47.000000', b'0', 'Vous avez été ajouté au projet : La Soumission comme passion#3b6aba6e-d658-44e4-a047-abf025bbd6c9', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 28, NULL),
(134, '2025-11-15 12:44:47.000000', b'0', 'Vous avez été ajouté au projet : La Soumission comme passion#3b6aba6e-d658-44e4-a047-abf025bbd6c9', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 28, NULL),
(135, '2025-11-15 12:44:47.000000', b'0', 'Vous avez été ajouté au projet : La Soumission comme passion#3b6aba6e-d658-44e4-a047-abf025bbd6c9', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 28, NULL),
(136, '2025-11-15 12:44:47.000000', b'0', 'Vous avez été ajouté au projet : La Soumission comme passion#3b6aba6e-d658-44e4-a047-abf025bbd6c9', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 28, NULL),
(137, '2025-11-15 12:45:44.000000', b'0', 'Vous avez été ajouté au projet : LOdeur du sanglier#07fc829e-22bc-49c0-9b03-ef5f42f27a23', 'AFFECTATION_MEMBRE_PROJET', 'salma', 29, NULL),
(138, '2025-11-15 12:45:44.000000', b'0', 'Vous avez été ajouté au projet : LOdeur du sanglier#07fc829e-22bc-49c0-9b03-ef5f42f27a23', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 29, NULL),
(139, '2025-11-15 12:45:44.000000', b'0', 'Vous avez été ajouté au projet : LOdeur du sanglier#07fc829e-22bc-49c0-9b03-ef5f42f27a23', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 29, NULL),
(140, '2025-11-15 12:45:44.000000', b'0', 'Vous avez été ajouté au projet : LOdeur du sanglier#07fc829e-22bc-49c0-9b03-ef5f42f27a23', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 29, NULL),
(141, '2025-11-15 12:45:44.000000', b'0', 'Vous avez été ajouté au projet : LOdeur du sanglier#07fc829e-22bc-49c0-9b03-ef5f42f27a23', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 29, NULL),
(142, '2025-11-15 12:46:27.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#6fd88b51-f0ea-4408-9bbd-16f7f7a704c1', 'AFFECTATION_MEMBRE_PROJET', 'salma', 30, NULL),
(143, '2025-11-15 12:46:27.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#6fd88b51-f0ea-4408-9bbd-16f7f7a704c1', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 30, NULL),
(144, '2025-11-15 12:46:27.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#6fd88b51-f0ea-4408-9bbd-16f7f7a704c1', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 30, NULL),
(145, '2025-11-15 12:46:27.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#6fd88b51-f0ea-4408-9bbd-16f7f7a704c1', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 30, NULL),
(146, '2025-11-15 12:46:27.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#6fd88b51-f0ea-4408-9bbd-16f7f7a704c1', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 30, NULL),
(147, '2025-11-15 12:47:11.000000', b'0', 'Vous avez été ajouté au projet : Le Siècle de la rue voisine#c861104a-cf4c-4505-9d7d-69d2403b9c80', 'AFFECTATION_MEMBRE_PROJET', 'salma', 31, NULL),
(148, '2025-11-15 12:47:11.000000', b'0', 'Vous avez été ajouté au projet : Le Siècle de la rue voisine#c861104a-cf4c-4505-9d7d-69d2403b9c80', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 31, NULL),
(149, '2025-11-15 12:47:11.000000', b'0', 'Vous avez été ajouté au projet : Le Siècle de la rue voisine#c861104a-cf4c-4505-9d7d-69d2403b9c80', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 31, NULL),
(150, '2025-11-15 12:47:11.000000', b'0', 'Vous avez été ajouté au projet : Le Siècle de la rue voisine#c861104a-cf4c-4505-9d7d-69d2403b9c80', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 31, NULL),
(151, '2025-11-15 12:47:11.000000', b'0', 'Vous avez été ajouté au projet : Le Siècle de la rue voisine#c861104a-cf4c-4505-9d7d-69d2403b9c80', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 31, NULL),
(152, '2025-11-15 12:47:57.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#eb782f08-775c-4fc2-afb5-69dae431389f', 'AFFECTATION_MEMBRE_PROJET', 'salma', 32, NULL),
(153, '2025-11-15 12:47:57.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#eb782f08-775c-4fc2-afb5-69dae431389f', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 32, NULL),
(154, '2025-11-15 12:47:57.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#eb782f08-775c-4fc2-afb5-69dae431389f', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 32, NULL),
(155, '2025-11-15 12:47:57.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#eb782f08-775c-4fc2-afb5-69dae431389f', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 32, NULL),
(156, '2025-11-15 12:47:57.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#eb782f08-775c-4fc2-afb5-69dae431389f', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 32, NULL),
(157, '2025-11-15 12:48:41.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#c2636e30-2335-4c4f-b452-b78fbd94f2cc', 'AFFECTATION_MEMBRE_PROJET', 'salma', 33, NULL),
(158, '2025-11-15 12:48:41.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#c2636e30-2335-4c4f-b452-b78fbd94f2cc', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 33, NULL),
(159, '2025-11-15 12:48:41.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#c2636e30-2335-4c4f-b452-b78fbd94f2cc', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 33, NULL),
(160, '2025-11-15 12:48:41.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#c2636e30-2335-4c4f-b452-b78fbd94f2cc', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 33, NULL),
(161, '2025-11-15 12:48:41.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#c2636e30-2335-4c4f-b452-b78fbd94f2cc', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 33, NULL),
(162, '2025-11-15 12:49:24.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#dbae7df2-7bd7-45e6-8cb4-1d4fea434b46', 'AFFECTATION_MEMBRE_PROJET', 'salma', 34, NULL),
(163, '2025-11-15 12:49:24.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#dbae7df2-7bd7-45e6-8cb4-1d4fea434b46', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 34, NULL),
(164, '2025-11-15 12:49:24.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#dbae7df2-7bd7-45e6-8cb4-1d4fea434b46', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 34, NULL),
(165, '2025-11-15 12:49:24.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#dbae7df2-7bd7-45e6-8cb4-1d4fea434b46', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 34, NULL),
(166, '2025-11-15 12:49:24.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#dbae7df2-7bd7-45e6-8cb4-1d4fea434b46', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 34, NULL),
(167, '2025-11-15 12:50:08.000000', b'0', 'Vous avez été ajouté au projet : LOdeur du sanglier#cf70e0b9-66e8-47c6-86b4-ed51e3b1f20a', 'AFFECTATION_MEMBRE_PROJET', 'salma', 35, NULL),
(168, '2025-11-15 12:50:08.000000', b'0', 'Vous avez été ajouté au projet : LOdeur du sanglier#cf70e0b9-66e8-47c6-86b4-ed51e3b1f20a', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 35, NULL),
(169, '2025-11-15 12:50:08.000000', b'0', 'Vous avez été ajouté au projet : LOdeur du sanglier#cf70e0b9-66e8-47c6-86b4-ed51e3b1f20a', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 35, NULL),
(170, '2025-11-15 12:50:08.000000', b'0', 'Vous avez été ajouté au projet : LOdeur du sanglier#cf70e0b9-66e8-47c6-86b4-ed51e3b1f20a', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 35, NULL),
(171, '2025-11-15 12:50:08.000000', b'0', 'Vous avez été ajouté au projet : LOdeur du sanglier#cf70e0b9-66e8-47c6-86b4-ed51e3b1f20a', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 35, NULL),
(172, '2025-11-15 12:50:51.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#ba109426-051b-4dfa-858a-ed849d57f0b6', 'AFFECTATION_MEMBRE_PROJET', 'salma', 36, NULL),
(173, '2025-11-15 12:50:51.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#ba109426-051b-4dfa-858a-ed849d57f0b6', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 36, NULL),
(174, '2025-11-15 12:50:51.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#ba109426-051b-4dfa-858a-ed849d57f0b6', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 36, NULL),
(175, '2025-11-15 12:50:51.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#ba109426-051b-4dfa-858a-ed849d57f0b6', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 36, NULL),
(176, '2025-11-15 12:50:51.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#ba109426-051b-4dfa-858a-ed849d57f0b6', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 36, NULL),
(177, '2025-11-15 12:51:35.000000', b'0', 'Vous avez été ajouté au projet : Le Siècle de la rue voisine#c453dd7e-06fd-4b29-8217-34367698271a', 'AFFECTATION_MEMBRE_PROJET', 'salma', 37, NULL),
(178, '2025-11-15 12:51:35.000000', b'0', 'Vous avez été ajouté au projet : Le Siècle de la rue voisine#c453dd7e-06fd-4b29-8217-34367698271a', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 37, NULL),
(179, '2025-11-15 12:51:35.000000', b'0', 'Vous avez été ajouté au projet : Le Siècle de la rue voisine#c453dd7e-06fd-4b29-8217-34367698271a', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 37, NULL),
(180, '2025-11-15 12:51:35.000000', b'0', 'Vous avez été ajouté au projet : Le Siècle de la rue voisine#c453dd7e-06fd-4b29-8217-34367698271a', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 37, NULL),
(181, '2025-11-15 12:51:35.000000', b'0', 'Vous avez été ajouté au projet : Le Siècle de la rue voisine#c453dd7e-06fd-4b29-8217-34367698271a', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 37, NULL),
(182, '2025-11-15 12:57:17.000000', b'0', 'Vous avez été ajouté au projet : Le Couloir de tous les mépris#73d23a6e-3a7c-490b-a6ad-373f5f8aa8fc', 'AFFECTATION_MEMBRE_PROJET', 'salma', 38, NULL),
(183, '2025-11-15 12:57:17.000000', b'0', 'Vous avez été ajouté au projet : Le Couloir de tous les mépris#73d23a6e-3a7c-490b-a6ad-373f5f8aa8fc', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 38, NULL),
(184, '2025-11-15 12:57:17.000000', b'0', 'Vous avez été ajouté au projet : Le Couloir de tous les mépris#73d23a6e-3a7c-490b-a6ad-373f5f8aa8fc', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 38, NULL),
(185, '2025-11-15 12:57:17.000000', b'0', 'Vous avez été ajouté au projet : Le Couloir de tous les mépris#73d23a6e-3a7c-490b-a6ad-373f5f8aa8fc', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 38, NULL),
(186, '2025-11-15 12:57:17.000000', b'0', 'Vous avez été ajouté au projet : Le Couloir de tous les mépris#73d23a6e-3a7c-490b-a6ad-373f5f8aa8fc', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 38, NULL),
(187, '2025-11-15 12:58:23.000000', b'0', 'Vous avez été ajouté au projet : Le Siècle de la rue voisine#3b7ee3a8-8987-4b30-b399-f48f875f53ea', 'AFFECTATION_MEMBRE_PROJET', 'salma', 39, NULL),
(188, '2025-11-15 12:58:23.000000', b'0', 'Vous avez été ajouté au projet : Le Siècle de la rue voisine#3b7ee3a8-8987-4b30-b399-f48f875f53ea', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 39, NULL),
(189, '2025-11-15 12:58:23.000000', b'0', 'Vous avez été ajouté au projet : Le Siècle de la rue voisine#3b7ee3a8-8987-4b30-b399-f48f875f53ea', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 39, NULL),
(190, '2025-11-15 12:58:23.000000', b'0', 'Vous avez été ajouté au projet : Le Siècle de la rue voisine#3b7ee3a8-8987-4b30-b399-f48f875f53ea', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 39, NULL),
(191, '2025-11-15 12:58:23.000000', b'0', 'Vous avez été ajouté au projet : Le Siècle de la rue voisine#3b7ee3a8-8987-4b30-b399-f48f875f53ea', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 39, NULL),
(192, '2025-11-15 12:59:13.000000', b'0', 'Vous avez été ajouté au projet : LOdyssée invisible#1264156d-8ec8-4357-bfcf-89bee0471a75', 'AFFECTATION_MEMBRE_PROJET', 'salma', 40, NULL),
(193, '2025-11-15 12:59:13.000000', b'0', 'Vous avez été ajouté au projet : LOdyssée invisible#1264156d-8ec8-4357-bfcf-89bee0471a75', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 40, NULL),
(194, '2025-11-15 12:59:13.000000', b'0', 'Vous avez été ajouté au projet : LOdyssée invisible#1264156d-8ec8-4357-bfcf-89bee0471a75', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 40, NULL),
(195, '2025-11-15 12:59:13.000000', b'0', 'Vous avez été ajouté au projet : LOdyssée invisible#1264156d-8ec8-4357-bfcf-89bee0471a75', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 40, NULL),
(196, '2025-11-15 12:59:13.000000', b'0', 'Vous avez été ajouté au projet : LOdyssée invisible#1264156d-8ec8-4357-bfcf-89bee0471a75', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 40, NULL),
(197, '2025-11-15 13:01:58.000000', b'0', 'Vous avez été ajouté au projet : La Peau des savants#c9bb16d0-377d-47ea-b31e-c227b8b1bf95', 'AFFECTATION_MEMBRE_PROJET', 'salma', 41, NULL),
(198, '2025-11-15 13:01:58.000000', b'0', 'Vous avez été ajouté au projet : La Peau des savants#c9bb16d0-377d-47ea-b31e-c227b8b1bf95', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 41, NULL),
(199, '2025-11-15 13:01:58.000000', b'0', 'Vous avez été ajouté au projet : La Peau des savants#c9bb16d0-377d-47ea-b31e-c227b8b1bf95', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 41, NULL),
(200, '2025-11-15 13:01:58.000000', b'0', 'Vous avez été ajouté au projet : La Peau des savants#c9bb16d0-377d-47ea-b31e-c227b8b1bf95', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 41, NULL),
(201, '2025-11-15 13:01:58.000000', b'0', 'Vous avez été ajouté au projet : La Peau des savants#c9bb16d0-377d-47ea-b31e-c227b8b1bf95', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 41, NULL),
(202, '2025-11-15 13:07:15.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#8a9c4874-01de-497a-b90b-4a392026c1c2', 'AFFECTATION_MEMBRE_PROJET', 'salma', 42, NULL),
(203, '2025-11-15 13:07:15.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#8a9c4874-01de-497a-b90b-4a392026c1c2', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 42, NULL),
(204, '2025-11-15 13:07:15.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#8a9c4874-01de-497a-b90b-4a392026c1c2', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 42, NULL),
(205, '2025-11-15 13:07:15.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#8a9c4874-01de-497a-b90b-4a392026c1c2', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 42, NULL),
(206, '2025-11-15 13:07:15.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#8a9c4874-01de-497a-b90b-4a392026c1c2', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 42, NULL),
(207, '2025-11-15 14:06:03.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#374ead0f-44e5-4322-9b90-29a03a3a6ca9', 'AFFECTATION_MEMBRE_PROJET', 'salma', 43, NULL),
(208, '2025-11-15 14:06:03.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#374ead0f-44e5-4322-9b90-29a03a3a6ca9', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 43, NULL),
(209, '2025-11-15 14:06:03.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#374ead0f-44e5-4322-9b90-29a03a3a6ca9', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 43, NULL),
(210, '2025-11-15 14:06:03.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#374ead0f-44e5-4322-9b90-29a03a3a6ca9', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 43, NULL),
(211, '2025-11-15 14:06:03.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#374ead0f-44e5-4322-9b90-29a03a3a6ca9', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 43, NULL),
(212, '2025-11-15 14:06:59.000000', b'0', 'Vous avez été ajouté au projet : LOdeur du sanglier#69135a07-fb4a-4ead-9f83-aa74fbe8a43e', 'AFFECTATION_MEMBRE_PROJET', 'salma', 44, NULL),
(213, '2025-11-15 14:06:59.000000', b'0', 'Vous avez été ajouté au projet : LOdeur du sanglier#69135a07-fb4a-4ead-9f83-aa74fbe8a43e', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 44, NULL),
(214, '2025-11-15 14:06:59.000000', b'0', 'Vous avez été ajouté au projet : LOdeur du sanglier#69135a07-fb4a-4ead-9f83-aa74fbe8a43e', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 44, NULL),
(215, '2025-11-15 14:06:59.000000', b'0', 'Vous avez été ajouté au projet : LOdeur du sanglier#69135a07-fb4a-4ead-9f83-aa74fbe8a43e', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 44, NULL),
(216, '2025-11-15 14:06:59.000000', b'0', 'Vous avez été ajouté au projet : LOdeur du sanglier#69135a07-fb4a-4ead-9f83-aa74fbe8a43e', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 44, NULL),
(217, '2025-11-15 14:07:53.000000', b'0', 'Vous avez été ajouté au projet : La Promise du voyeur#701f7456-c7a0-4c5a-95b2-51a2cedcc6d0', 'AFFECTATION_MEMBRE_PROJET', 'salma', 45, NULL),
(218, '2025-11-15 14:07:53.000000', b'0', 'Vous avez été ajouté au projet : La Promise du voyeur#701f7456-c7a0-4c5a-95b2-51a2cedcc6d0', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 45, NULL),
(219, '2025-11-15 14:07:53.000000', b'0', 'Vous avez été ajouté au projet : La Promise du voyeur#701f7456-c7a0-4c5a-95b2-51a2cedcc6d0', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 45, NULL),
(220, '2025-11-15 14:07:53.000000', b'0', 'Vous avez été ajouté au projet : La Promise du voyeur#701f7456-c7a0-4c5a-95b2-51a2cedcc6d0', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 45, NULL),
(221, '2025-11-15 14:07:53.000000', b'0', 'Vous avez été ajouté au projet : La Promise du voyeur#701f7456-c7a0-4c5a-95b2-51a2cedcc6d0', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 45, NULL),
(222, '2025-11-15 14:12:40.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#ebd2682c-2af8-4102-8788-55b3613659cc', 'AFFECTATION_MEMBRE_PROJET', 'salma', 46, NULL),
(223, '2025-11-15 14:12:40.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#ebd2682c-2af8-4102-8788-55b3613659cc', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 46, NULL),
(224, '2025-11-15 14:12:40.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#ebd2682c-2af8-4102-8788-55b3613659cc', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 46, NULL),
(225, '2025-11-15 14:12:40.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#ebd2682c-2af8-4102-8788-55b3613659cc', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 46, NULL),
(226, '2025-11-15 14:12:40.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#ebd2682c-2af8-4102-8788-55b3613659cc', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 46, NULL),
(227, '2025-11-15 14:13:32.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#08743ce4-6fa5-4a49-984d-0ef930153b89', 'AFFECTATION_MEMBRE_PROJET', 'salma', 47, NULL),
(228, '2025-11-15 14:13:32.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#08743ce4-6fa5-4a49-984d-0ef930153b89', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 47, NULL),
(229, '2025-11-15 14:13:32.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#08743ce4-6fa5-4a49-984d-0ef930153b89', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 47, NULL),
(230, '2025-11-15 14:13:32.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#08743ce4-6fa5-4a49-984d-0ef930153b89', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 47, NULL),
(231, '2025-11-15 14:13:32.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#08743ce4-6fa5-4a49-984d-0ef930153b89', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 47, NULL),
(232, '2025-11-15 14:14:19.000000', b'0', 'Vous avez été ajouté au projet : LOdyssée invisible#66ea3148-a7ad-4f4f-811f-d0dfa8143617', 'AFFECTATION_MEMBRE_PROJET', 'salma', 48, NULL),
(233, '2025-11-15 14:14:19.000000', b'0', 'Vous avez été ajouté au projet : LOdyssée invisible#66ea3148-a7ad-4f4f-811f-d0dfa8143617', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 48, NULL),
(234, '2025-11-15 14:14:19.000000', b'0', 'Vous avez été ajouté au projet : LOdyssée invisible#66ea3148-a7ad-4f4f-811f-d0dfa8143617', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 48, NULL),
(235, '2025-11-15 14:14:19.000000', b'0', 'Vous avez été ajouté au projet : LOdyssée invisible#66ea3148-a7ad-4f4f-811f-d0dfa8143617', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 48, NULL),
(236, '2025-11-15 14:14:19.000000', b'0', 'Vous avez été ajouté au projet : LOdyssée invisible#66ea3148-a7ad-4f4f-811f-d0dfa8143617', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 48, NULL),
(237, '2025-11-15 14:15:00.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#f2a25a96-cfd2-4313-a885-8c532eab46aa', 'AFFECTATION_MEMBRE_PROJET', 'salma', 49, NULL),
(238, '2025-11-15 14:15:00.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#f2a25a96-cfd2-4313-a885-8c532eab46aa', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 49, NULL),
(239, '2025-11-15 14:15:00.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#f2a25a96-cfd2-4313-a885-8c532eab46aa', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 49, NULL),
(240, '2025-11-15 14:15:00.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#f2a25a96-cfd2-4313-a885-8c532eab46aa', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 49, NULL),
(241, '2025-11-15 14:15:00.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#f2a25a96-cfd2-4313-a885-8c532eab46aa', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 49, NULL),
(242, '2025-11-15 14:16:04.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#cdb4ec57-c79d-48e5-bd89-7f3b621753f9', 'AFFECTATION_MEMBRE_PROJET', 'salma', 50, NULL),
(243, '2025-11-15 14:16:04.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#cdb4ec57-c79d-48e5-bd89-7f3b621753f9', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 50, NULL),
(244, '2025-11-15 14:16:04.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#cdb4ec57-c79d-48e5-bd89-7f3b621753f9', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 50, NULL),
(245, '2025-11-15 14:16:04.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#cdb4ec57-c79d-48e5-bd89-7f3b621753f9', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 50, NULL),
(246, '2025-11-15 14:16:04.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#cdb4ec57-c79d-48e5-bd89-7f3b621753f9', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 50, NULL),
(247, '2025-11-15 14:17:17.000000', b'0', 'Vous avez été ajouté au projet : La Peau des savants#d9e1a17a-8f67-4617-b7fd-71d635f1b8c2', 'AFFECTATION_MEMBRE_PROJET', 'salma', 51, NULL),
(248, '2025-11-15 14:17:17.000000', b'0', 'Vous avez été ajouté au projet : La Peau des savants#d9e1a17a-8f67-4617-b7fd-71d635f1b8c2', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 51, NULL),
(249, '2025-11-15 14:17:17.000000', b'0', 'Vous avez été ajouté au projet : La Peau des savants#d9e1a17a-8f67-4617-b7fd-71d635f1b8c2', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 51, NULL),
(250, '2025-11-15 14:17:17.000000', b'0', 'Vous avez été ajouté au projet : La Peau des savants#d9e1a17a-8f67-4617-b7fd-71d635f1b8c2', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 51, NULL),
(251, '2025-11-15 14:17:17.000000', b'0', 'Vous avez été ajouté au projet : La Peau des savants#d9e1a17a-8f67-4617-b7fd-71d635f1b8c2', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 51, NULL),
(252, '2025-11-15 14:18:33.000000', b'0', 'Vous avez été ajouté au projet : Pourquoi je mens ?#89f53cdd-23a5-47c9-a11e-ba3f46ab5f84', 'AFFECTATION_MEMBRE_PROJET', 'salma', 52, NULL),
(253, '2025-11-15 14:18:33.000000', b'0', 'Vous avez été ajouté au projet : Pourquoi je mens ?#89f53cdd-23a5-47c9-a11e-ba3f46ab5f84', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 52, NULL),
(254, '2025-11-15 14:18:33.000000', b'0', 'Vous avez été ajouté au projet : Pourquoi je mens ?#89f53cdd-23a5-47c9-a11e-ba3f46ab5f84', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 52, NULL),
(255, '2025-11-15 14:18:33.000000', b'0', 'Vous avez été ajouté au projet : Pourquoi je mens ?#89f53cdd-23a5-47c9-a11e-ba3f46ab5f84', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 52, NULL),
(256, '2025-11-15 14:18:33.000000', b'0', 'Vous avez été ajouté au projet : Pourquoi je mens ?#89f53cdd-23a5-47c9-a11e-ba3f46ab5f84', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 52, NULL),
(257, '2025-11-15 14:19:43.000000', b'0', 'Vous avez été ajouté au projet : Pourquoi je mens ?#f66b0d3d-915a-4a05-be17-47a1a009cf0a', 'AFFECTATION_MEMBRE_PROJET', 'salma', 53, NULL),
(258, '2025-11-15 14:19:43.000000', b'0', 'Vous avez été ajouté au projet : Pourquoi je mens ?#f66b0d3d-915a-4a05-be17-47a1a009cf0a', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 53, NULL),
(259, '2025-11-15 14:19:43.000000', b'0', 'Vous avez été ajouté au projet : Pourquoi je mens ?#f66b0d3d-915a-4a05-be17-47a1a009cf0a', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 53, NULL),
(260, '2025-11-15 14:19:43.000000', b'0', 'Vous avez été ajouté au projet : Pourquoi je mens ?#f66b0d3d-915a-4a05-be17-47a1a009cf0a', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 53, NULL),
(261, '2025-11-15 14:19:43.000000', b'0', 'Vous avez été ajouté au projet : Pourquoi je mens ?#f66b0d3d-915a-4a05-be17-47a1a009cf0a', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 53, NULL),
(262, '2025-11-15 14:20:54.000000', b'0', 'Vous avez été ajouté au projet : LOdeur du sanglier#a5098c4d-a3d7-4694-9b8d-456d05f284c0', 'AFFECTATION_MEMBRE_PROJET', 'salma', 54, NULL),
(263, '2025-11-15 14:20:54.000000', b'0', 'Vous avez été ajouté au projet : LOdeur du sanglier#a5098c4d-a3d7-4694-9b8d-456d05f284c0', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 54, NULL),
(264, '2025-11-15 14:20:54.000000', b'0', 'Vous avez été ajouté au projet : LOdeur du sanglier#a5098c4d-a3d7-4694-9b8d-456d05f284c0', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 54, NULL),
(265, '2025-11-15 14:20:54.000000', b'0', 'Vous avez été ajouté au projet : LOdeur du sanglier#a5098c4d-a3d7-4694-9b8d-456d05f284c0', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 54, NULL),
(266, '2025-11-15 14:20:54.000000', b'0', 'Vous avez été ajouté au projet : LOdeur du sanglier#a5098c4d-a3d7-4694-9b8d-456d05f284c0', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 54, NULL),
(267, '2025-11-15 14:22:14.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#a2b1a1a5-8a49-4d15-aeb2-908bf5286421', 'AFFECTATION_MEMBRE_PROJET', 'salma', 55, NULL),
(268, '2025-11-15 14:22:14.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#a2b1a1a5-8a49-4d15-aeb2-908bf5286421', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 55, NULL);
INSERT INTO `notifications` (`id`, `date`, `lue`, `message`, `type`, `destinataire_matricule`, `projet_lie_numero_projet`, `tache_liee_id`) VALUES
(269, '2025-11-15 14:22:14.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#a2b1a1a5-8a49-4d15-aeb2-908bf5286421', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 55, NULL),
(270, '2025-11-15 14:22:14.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#a2b1a1a5-8a49-4d15-aeb2-908bf5286421', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 55, NULL),
(271, '2025-11-15 14:22:14.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#a2b1a1a5-8a49-4d15-aeb2-908bf5286421', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 55, NULL),
(272, '2025-11-15 14:23:31.000000', b'0', 'Vous avez été ajouté au projet : Le Siècle de la rue voisine#7403ef94-44b0-4974-86c4-8c1aad30d0c6', 'AFFECTATION_MEMBRE_PROJET', 'salma', 56, NULL),
(273, '2025-11-15 14:23:31.000000', b'0', 'Vous avez été ajouté au projet : Le Siècle de la rue voisine#7403ef94-44b0-4974-86c4-8c1aad30d0c6', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 56, NULL),
(274, '2025-11-15 14:23:31.000000', b'0', 'Vous avez été ajouté au projet : Le Siècle de la rue voisine#7403ef94-44b0-4974-86c4-8c1aad30d0c6', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 56, NULL),
(275, '2025-11-15 14:23:31.000000', b'0', 'Vous avez été ajouté au projet : Le Siècle de la rue voisine#7403ef94-44b0-4974-86c4-8c1aad30d0c6', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 56, NULL),
(276, '2025-11-15 14:23:31.000000', b'0', 'Vous avez été ajouté au projet : Le Siècle de la rue voisine#7403ef94-44b0-4974-86c4-8c1aad30d0c6', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 56, NULL),
(277, '2025-11-15 15:06:40.000000', b'0', 'Vous avez été ajouté à la tâche : La Soumission comme passion#427beba0-9b7d-48bc-8338-8d51e9368567', 'TACHE_ASSIGNATION', 'salma', 1, 1),
(278, '2025-11-15 15:06:40.000000', b'0', 'Vous avez été ajouté à la tâche : La Soumission comme passion#427beba0-9b7d-48bc-8338-8d51e9368567', 'TACHE_ASSIGNATION', 'pouka', 1, 1),
(279, '2025-11-15 15:06:40.000000', b'0', 'Vous avez été ajouté à la tâche : La Soumission comme passion#427beba0-9b7d-48bc-8338-8d51e9368567', 'TACHE_ASSIGNATION', 'sameh', 1, 1),
(280, '2025-11-15 15:09:23.000000', b'0', 'Vous avez été ajouté à la tâche : La Soumission comme passion#b2ea1ecf-6b84-40de-ae16-27a270c7d265', 'TACHE_ASSIGNATION', 'salma', 1, 2),
(281, '2025-11-15 15:09:23.000000', b'0', 'Vous avez été ajouté à la tâche : La Soumission comme passion#b2ea1ecf-6b84-40de-ae16-27a270c7d265', 'TACHE_ASSIGNATION', 'pouka', 1, 2),
(282, '2025-11-15 15:09:23.000000', b'0', 'Vous avez été ajouté à la tâche : La Soumission comme passion#b2ea1ecf-6b84-40de-ae16-27a270c7d265', 'TACHE_ASSIGNATION', 'sameh', 1, 2),
(283, '2025-11-15 15:27:59.000000', b'0', 'Vous avez été ajouté à la tâche : LOdyssée invisible#d9e26837-5082-46c9-8c11-4b806b500d05', 'TACHE_ASSIGNATION', 'salma', 1, 3),
(284, '2025-11-15 15:27:59.000000', b'0', 'Vous avez été ajouté à la tâche : LOdyssée invisible#d9e26837-5082-46c9-8c11-4b806b500d05', 'TACHE_ASSIGNATION', 'pouka', 1, 3),
(285, '2025-11-15 15:27:59.000000', b'0', 'Vous avez été ajouté à la tâche : LOdyssée invisible#d9e26837-5082-46c9-8c11-4b806b500d05', 'TACHE_ASSIGNATION', 'sameh', 1, 3),
(286, '2025-11-15 15:29:56.000000', b'0', 'Vous avez été ajouté à la tâche : La Peau des savants#81953a1a-18a9-482c-b7a4-ef423262246d', 'TACHE_ASSIGNATION', 'salma', 1, 4),
(287, '2025-11-15 15:29:56.000000', b'0', 'Vous avez été ajouté à la tâche : La Peau des savants#81953a1a-18a9-482c-b7a4-ef423262246d', 'TACHE_ASSIGNATION', 'sameh', 1, 4),
(288, '2025-11-15 15:31:42.000000', b'0', 'Vous avez été ajouté à la tâche : La Discipline des orphelins#5fb5b00c-f854-4191-9b1b-e5ccf9c87c63', 'TACHE_ASSIGNATION', 'salma', 1, 5),
(289, '2025-11-15 15:31:42.000000', b'0', 'Vous avez été ajouté à la tâche : La Discipline des orphelins#5fb5b00c-f854-4191-9b1b-e5ccf9c87c63', 'TACHE_ASSIGNATION', 'sameh', 1, 5),
(290, '2025-11-15 16:45:21.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#80dda495-f412-4982-af4b-976a97cfd6f6', 'AFFECTATION_MEMBRE_PROJET', 'salma', 57, NULL),
(291, '2025-11-15 16:45:21.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#80dda495-f412-4982-af4b-976a97cfd6f6', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 57, NULL),
(292, '2025-11-15 16:45:21.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#80dda495-f412-4982-af4b-976a97cfd6f6', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 57, NULL),
(293, '2025-11-15 16:45:21.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#80dda495-f412-4982-af4b-976a97cfd6f6', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 57, NULL),
(294, '2025-11-15 16:45:21.000000', b'0', 'Vous avez été ajouté au projet : La progéniture du mal#80dda495-f412-4982-af4b-976a97cfd6f6', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 57, NULL),
(295, '2025-11-15 16:46:44.000000', b'0', 'Vous avez été ajouté au projet : La Peau des savants#856788f1-1d41-43ec-b53b-2bc9a23752ab', 'AFFECTATION_MEMBRE_PROJET', 'salma', 58, NULL),
(296, '2025-11-15 16:46:44.000000', b'0', 'Vous avez été ajouté au projet : La Peau des savants#856788f1-1d41-43ec-b53b-2bc9a23752ab', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 58, NULL),
(297, '2025-11-15 16:46:44.000000', b'0', 'Vous avez été ajouté au projet : La Peau des savants#856788f1-1d41-43ec-b53b-2bc9a23752ab', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 58, NULL),
(298, '2025-11-15 16:46:44.000000', b'0', 'Vous avez été ajouté au projet : La Peau des savants#856788f1-1d41-43ec-b53b-2bc9a23752ab', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 58, NULL),
(299, '2025-11-15 16:46:44.000000', b'0', 'Vous avez été ajouté au projet : La Peau des savants#856788f1-1d41-43ec-b53b-2bc9a23752ab', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 58, NULL),
(300, '2025-11-15 16:47:58.000000', b'0', 'Vous avez été ajouté au projet : La Soumission comme passion#da04d2d6-bfa2-41ea-af8e-7ad78d470e0a', 'AFFECTATION_MEMBRE_PROJET', 'salma', 59, NULL),
(301, '2025-11-15 16:47:58.000000', b'0', 'Vous avez été ajouté au projet : La Soumission comme passion#da04d2d6-bfa2-41ea-af8e-7ad78d470e0a', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 59, NULL),
(302, '2025-11-15 16:47:58.000000', b'0', 'Vous avez été ajouté au projet : La Soumission comme passion#da04d2d6-bfa2-41ea-af8e-7ad78d470e0a', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 59, NULL),
(303, '2025-11-15 16:47:58.000000', b'0', 'Vous avez été ajouté au projet : La Soumission comme passion#da04d2d6-bfa2-41ea-af8e-7ad78d470e0a', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 59, NULL),
(304, '2025-11-15 16:47:58.000000', b'0', 'Vous avez été ajouté au projet : La Soumission comme passion#da04d2d6-bfa2-41ea-af8e-7ad78d470e0a', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 59, NULL),
(305, '2025-11-15 16:57:23.000000', b'0', 'Vous avez été ajouté au projet : La Promise du voyeur#a62aa08a-f057-4c1f-82ef-97f7cb0167f5', 'AFFECTATION_MEMBRE_PROJET', 'salma', 60, NULL),
(306, '2025-11-15 16:57:23.000000', b'0', 'Vous avez été ajouté au projet : La Promise du voyeur#a62aa08a-f057-4c1f-82ef-97f7cb0167f5', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 60, NULL),
(307, '2025-11-15 16:57:23.000000', b'0', 'Vous avez été ajouté au projet : La Promise du voyeur#a62aa08a-f057-4c1f-82ef-97f7cb0167f5', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 60, NULL),
(308, '2025-11-15 16:57:23.000000', b'0', 'Vous avez été ajouté au projet : La Promise du voyeur#a62aa08a-f057-4c1f-82ef-97f7cb0167f5', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 60, NULL),
(309, '2025-11-15 16:57:23.000000', b'0', 'Vous avez été ajouté au projet : La Promise du voyeur#a62aa08a-f057-4c1f-82ef-97f7cb0167f5', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 60, NULL),
(310, '2025-11-15 16:59:14.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#a9a4c3fb-d321-471f-86cc-b57a7de6e512', 'AFFECTATION_MEMBRE_PROJET', 'salma', 61, NULL),
(311, '2025-11-15 16:59:14.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#a9a4c3fb-d321-471f-86cc-b57a7de6e512', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 61, NULL),
(312, '2025-11-15 16:59:14.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#a9a4c3fb-d321-471f-86cc-b57a7de6e512', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 61, NULL),
(313, '2025-11-15 16:59:14.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#a9a4c3fb-d321-471f-86cc-b57a7de6e512', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 61, NULL),
(314, '2025-11-15 16:59:14.000000', b'0', 'Vous avez été ajouté au projet : Le Désir des femmes fortes#a9a4c3fb-d321-471f-86cc-b57a7de6e512', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 61, NULL),
(315, '2025-11-15 17:00:50.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#f8e6c723-ae9b-442a-a6aa-32d0167e514c', 'AFFECTATION_MEMBRE_PROJET', 'salma', 62, NULL),
(316, '2025-11-15 17:00:50.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#f8e6c723-ae9b-442a-a6aa-32d0167e514c', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 62, NULL),
(317, '2025-11-15 17:00:50.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#f8e6c723-ae9b-442a-a6aa-32d0167e514c', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 62, NULL),
(318, '2025-11-15 17:00:50.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#f8e6c723-ae9b-442a-a6aa-32d0167e514c', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 62, NULL),
(319, '2025-11-15 17:00:50.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#f8e6c723-ae9b-442a-a6aa-32d0167e514c', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 62, NULL),
(320, '2025-11-15 17:01:54.000000', b'0', 'Vous avez été ajouté au projet : Pourquoi je mens ?#d3469b60-10b4-4ad7-bd33-f951c6eca18d', 'AFFECTATION_MEMBRE_PROJET', 'salma', 63, NULL),
(321, '2025-11-15 17:01:54.000000', b'0', 'Vous avez été ajouté au projet : Pourquoi je mens ?#d3469b60-10b4-4ad7-bd33-f951c6eca18d', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 63, NULL),
(322, '2025-11-15 17:01:54.000000', b'0', 'Vous avez été ajouté au projet : Pourquoi je mens ?#d3469b60-10b4-4ad7-bd33-f951c6eca18d', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 63, NULL),
(323, '2025-11-15 17:01:54.000000', b'0', 'Vous avez été ajouté au projet : Pourquoi je mens ?#d3469b60-10b4-4ad7-bd33-f951c6eca18d', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 63, NULL),
(324, '2025-11-15 17:01:54.000000', b'0', 'Vous avez été ajouté au projet : Pourquoi je mens ?#d3469b60-10b4-4ad7-bd33-f951c6eca18d', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 63, NULL),
(325, '2025-11-15 17:03:10.000000', b'0', 'Vous avez été ajouté au projet : LOdeur du sanglier#24a6b07b-0674-4cc5-970f-371546157305', 'AFFECTATION_MEMBRE_PROJET', 'salma', 64, NULL),
(326, '2025-11-15 17:03:10.000000', b'0', 'Vous avez été ajouté au projet : LOdeur du sanglier#24a6b07b-0674-4cc5-970f-371546157305', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 64, NULL),
(327, '2025-11-15 17:03:10.000000', b'0', 'Vous avez été ajouté au projet : LOdeur du sanglier#24a6b07b-0674-4cc5-970f-371546157305', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 64, NULL),
(328, '2025-11-15 17:03:10.000000', b'0', 'Vous avez été ajouté au projet : LOdeur du sanglier#24a6b07b-0674-4cc5-970f-371546157305', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 64, NULL),
(329, '2025-11-15 17:03:10.000000', b'0', 'Vous avez été ajouté au projet : LOdeur du sanglier#24a6b07b-0674-4cc5-970f-371546157305', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 64, NULL),
(330, '2025-11-15 17:04:35.000000', b'0', 'Vous avez été ajouté au projet : Le Couloir de tous les mépris#f75bd573-3254-4df8-afe2-f38c9b1a4b10', 'AFFECTATION_MEMBRE_PROJET', 'salma', 65, NULL),
(331, '2025-11-15 17:04:35.000000', b'0', 'Vous avez été ajouté au projet : Le Couloir de tous les mépris#f75bd573-3254-4df8-afe2-f38c9b1a4b10', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 65, NULL),
(332, '2025-11-15 17:04:35.000000', b'0', 'Vous avez été ajouté au projet : Le Couloir de tous les mépris#f75bd573-3254-4df8-afe2-f38c9b1a4b10', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 65, NULL),
(333, '2025-11-15 17:04:35.000000', b'0', 'Vous avez été ajouté au projet : Le Couloir de tous les mépris#f75bd573-3254-4df8-afe2-f38c9b1a4b10', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 65, NULL),
(334, '2025-11-15 17:04:35.000000', b'0', 'Vous avez été ajouté au projet : Le Couloir de tous les mépris#f75bd573-3254-4df8-afe2-f38c9b1a4b10', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 65, NULL),
(335, '2025-11-15 17:05:37.000000', b'0', 'Vous avez été ajouté au projet : LOdeur du sanglier#e1e145a7-de08-46d3-8bc7-610cf6520c37', 'AFFECTATION_MEMBRE_PROJET', 'salma', 66, NULL),
(336, '2025-11-15 17:05:37.000000', b'0', 'Vous avez été ajouté au projet : LOdeur du sanglier#e1e145a7-de08-46d3-8bc7-610cf6520c37', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 66, NULL),
(337, '2025-11-15 17:05:37.000000', b'0', 'Vous avez été ajouté au projet : LOdeur du sanglier#e1e145a7-de08-46d3-8bc7-610cf6520c37', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 66, NULL),
(338, '2025-11-15 17:05:37.000000', b'0', 'Vous avez été ajouté au projet : LOdeur du sanglier#e1e145a7-de08-46d3-8bc7-610cf6520c37', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 66, NULL),
(339, '2025-11-15 17:05:37.000000', b'0', 'Vous avez été ajouté au projet : LOdeur du sanglier#e1e145a7-de08-46d3-8bc7-610cf6520c37', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 66, NULL),
(340, '2025-11-15 17:06:39.000000', b'0', 'Vous avez été ajouté au projet : LOdyssée invisible#987202fa-440c-48d0-93a9-70dd1ce3ab10', 'AFFECTATION_MEMBRE_PROJET', 'salma', 67, NULL),
(341, '2025-11-15 17:06:39.000000', b'0', 'Vous avez été ajouté au projet : LOdyssée invisible#987202fa-440c-48d0-93a9-70dd1ce3ab10', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 67, NULL),
(342, '2025-11-15 17:06:39.000000', b'0', 'Vous avez été ajouté au projet : LOdyssée invisible#987202fa-440c-48d0-93a9-70dd1ce3ab10', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 67, NULL),
(343, '2025-11-15 17:06:39.000000', b'0', 'Vous avez été ajouté au projet : LOdyssée invisible#987202fa-440c-48d0-93a9-70dd1ce3ab10', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 67, NULL),
(344, '2025-11-15 17:06:39.000000', b'0', 'Vous avez été ajouté au projet : LOdyssée invisible#987202fa-440c-48d0-93a9-70dd1ce3ab10', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 67, NULL),
(345, '2025-11-15 17:07:41.000000', b'0', 'Vous avez été ajouté au projet : LOdyssée invisible#839e1031-fe62-47c8-a43c-e24e8e8ff752', 'AFFECTATION_MEMBRE_PROJET', 'salma', 68, NULL),
(346, '2025-11-15 17:07:41.000000', b'0', 'Vous avez été ajouté au projet : LOdyssée invisible#839e1031-fe62-47c8-a43c-e24e8e8ff752', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 68, NULL),
(347, '2025-11-15 17:07:41.000000', b'0', 'Vous avez été ajouté au projet : LOdyssée invisible#839e1031-fe62-47c8-a43c-e24e8e8ff752', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 68, NULL),
(348, '2025-11-15 17:07:41.000000', b'0', 'Vous avez été ajouté au projet : LOdyssée invisible#839e1031-fe62-47c8-a43c-e24e8e8ff752', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 68, NULL),
(349, '2025-11-15 17:07:41.000000', b'0', 'Vous avez été ajouté au projet : LOdyssée invisible#839e1031-fe62-47c8-a43c-e24e8e8ff752', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 68, NULL),
(350, '2025-11-15 17:08:40.000000', b'0', 'Vous avez été ajouté au projet : Pourquoi je mens ?#dbed84b7-6fb1-4639-9e38-8565deb1df0c', 'AFFECTATION_MEMBRE_PROJET', 'salma', 69, NULL),
(351, '2025-11-15 17:08:40.000000', b'0', 'Vous avez été ajouté au projet : Pourquoi je mens ?#dbed84b7-6fb1-4639-9e38-8565deb1df0c', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 69, NULL),
(352, '2025-11-15 17:08:40.000000', b'0', 'Vous avez été ajouté au projet : Pourquoi je mens ?#dbed84b7-6fb1-4639-9e38-8565deb1df0c', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 69, NULL),
(353, '2025-11-15 17:08:40.000000', b'0', 'Vous avez été ajouté au projet : Pourquoi je mens ?#dbed84b7-6fb1-4639-9e38-8565deb1df0c', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 69, NULL),
(354, '2025-11-15 17:08:40.000000', b'0', 'Vous avez été ajouté au projet : Pourquoi je mens ?#dbed84b7-6fb1-4639-9e38-8565deb1df0c', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 69, NULL),
(355, '2025-11-15 17:09:38.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#ac22d00b-5431-4da1-9ecb-acc81d6f1437', 'AFFECTATION_MEMBRE_PROJET', 'salma', 70, NULL),
(356, '2025-11-15 17:09:38.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#ac22d00b-5431-4da1-9ecb-acc81d6f1437', 'AFFECTATION_MEMBRE_PROJET', 'chiraz', 70, NULL),
(357, '2025-11-15 17:09:38.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#ac22d00b-5431-4da1-9ecb-acc81d6f1437', 'AFFECTATION_MEMBRE_PROJET', 'fahed', 70, NULL),
(358, '2025-11-15 17:09:38.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#ac22d00b-5431-4da1-9ecb-acc81d6f1437', 'AFFECTATION_MEMBRE_PROJET', 'pouka', 70, NULL),
(359, '2025-11-15 17:09:38.000000', b'0', 'Vous avez été ajouté au projet : La Discipline des orphelins#ac22d00b-5431-4da1-9ecb-acc81d6f1437', 'AFFECTATION_MEMBRE_PROJET', 'sameh', 70, NULL),
(360, '2025-11-15 17:10:55.000000', b'0', 'Vous avez été ajouté à la tâche : LOdeur du sanglier#348e39f2-79a0-4085-9646-a1402e8776d8', 'TACHE_ASSIGNATION', 'salma', 1, 6),
(361, '2025-11-15 17:10:55.000000', b'0', 'Vous avez été ajouté à la tâche : LOdeur du sanglier#348e39f2-79a0-4085-9646-a1402e8776d8', 'TACHE_ASSIGNATION', 'sameh', 1, 6),
(362, '2025-11-15 17:12:26.000000', b'0', 'Vous avez été ajouté à la tâche : La Peau des savants#ae3c4db3-20f5-4d0f-b0c2-80903b8799c8', 'TACHE_ASSIGNATION', 'salma', 1, 7),
(363, '2025-11-15 17:12:26.000000', b'0', 'Vous avez été ajouté à la tâche : La Peau des savants#ae3c4db3-20f5-4d0f-b0c2-80903b8799c8', 'TACHE_ASSIGNATION', 'sameh', 1, 7),
(364, '2025-11-15 17:13:51.000000', b'0', 'Vous avez été ajouté à la tâche : Le Couloir de tous les mépris#9d9d6dbc-3742-41a7-8c87-d340044be7b8', 'TACHE_ASSIGNATION', 'salma', 1, 8),
(365, '2025-11-15 17:13:51.000000', b'0', 'Vous avez été ajouté à la tâche : Le Couloir de tous les mépris#9d9d6dbc-3742-41a7-8c87-d340044be7b8', 'TACHE_ASSIGNATION', 'sameh', 1, 8),
(366, '2025-11-15 17:15:32.000000', b'0', 'Vous avez été ajouté à la tâche : La Peau des savants#a2b802b8-be99-4598-9905-20ce2d1da14d', 'TACHE_ASSIGNATION', 'salma', 1, 9),
(367, '2025-11-15 17:15:32.000000', b'0', 'Vous avez été ajouté à la tâche : La Peau des savants#a2b802b8-be99-4598-9905-20ce2d1da14d', 'TACHE_ASSIGNATION', 'sameh', 1, 9),
(368, '2025-11-15 17:17:06.000000', b'0', 'Vous avez été ajouté à la tâche : LOdyssée invisible#360e0c2b-d068-447b-b01d-a077cc1f4f41', 'TACHE_ASSIGNATION', 'salma', 1, 10),
(369, '2025-11-15 17:17:06.000000', b'0', 'Vous avez été ajouté à la tâche : LOdyssée invisible#360e0c2b-d068-447b-b01d-a077cc1f4f41', 'TACHE_ASSIGNATION', 'sameh', 1, 10),
(370, '2025-11-15 17:18:30.000000', b'0', 'Vous avez été ajouté à la tâche : La Promise du voyeur#47f127c8-1d5e-4185-af36-0c425d0899d0', 'TACHE_ASSIGNATION', 'salma', 1, 11),
(371, '2025-11-15 17:18:30.000000', b'0', 'Vous avez été ajouté à la tâche : La Promise du voyeur#47f127c8-1d5e-4185-af36-0c425d0899d0', 'TACHE_ASSIGNATION', 'sameh', 1, 11),
(372, '2025-11-15 17:20:07.000000', b'0', 'Vous avez été ajouté à la tâche : La Discipline des orphelins#ac7df66f-321f-4dce-84bb-35fcd9d2adaf', 'TACHE_ASSIGNATION', 'salma', 1, 12),
(373, '2025-11-15 17:20:07.000000', b'0', 'Vous avez été ajouté à la tâche : La Discipline des orphelins#ac7df66f-321f-4dce-84bb-35fcd9d2adaf', 'TACHE_ASSIGNATION', 'sameh', 1, 12),
(374, '2025-11-15 17:21:24.000000', b'0', 'Vous avez été ajouté à la tâche : La Peau des savants#e332ce50-84bf-498c-9039-f9094fdb5331', 'TACHE_ASSIGNATION', 'salma', 1, 13),
(375, '2025-11-15 17:21:24.000000', b'0', 'Vous avez été ajouté à la tâche : La Peau des savants#e332ce50-84bf-498c-9039-f9094fdb5331', 'TACHE_ASSIGNATION', 'sameh', 1, 13),
(376, '2025-11-15 17:22:23.000000', b'0', 'Vous avez été ajouté à la tâche : La Promise du voyeur#5b288f46-0243-4dee-902c-1c7bf95ebcdb', 'TACHE_ASSIGNATION', 'salma', 1, 14),
(377, '2025-11-15 17:22:23.000000', b'0', 'Vous avez été ajouté à la tâche : La Promise du voyeur#5b288f46-0243-4dee-902c-1c7bf95ebcdb', 'TACHE_ASSIGNATION', 'sameh', 1, 14),
(378, '2025-11-15 17:23:23.000000', b'0', 'Vous avez été ajouté à la tâche : La progéniture du mal#b56c18b1-a56b-49c3-bf57-bb805095ea88', 'TACHE_ASSIGNATION', 'salma', 1, 15),
(379, '2025-11-15 17:23:23.000000', b'0', 'Vous avez été ajouté à la tâche : La progéniture du mal#b56c18b1-a56b-49c3-bf57-bb805095ea88', 'TACHE_ASSIGNATION', 'sameh', 1, 15),
(380, '2025-11-15 17:24:28.000000', b'0', 'Vous avez été ajouté à la tâche : Le Couloir de tous les mépris#efbe590a-e8a9-40ef-878a-d8974b7abcb3', 'TACHE_ASSIGNATION', 'salma', 1, 16),
(381, '2025-11-15 17:24:28.000000', b'0', 'Vous avez été ajouté à la tâche : Le Couloir de tous les mépris#efbe590a-e8a9-40ef-878a-d8974b7abcb3', 'TACHE_ASSIGNATION', 'sameh', 1, 16),
(382, '2025-11-15 17:25:28.000000', b'0', 'Vous avez été ajouté à la tâche : LOdyssée invisible#38d9f2a8-19a0-4b15-90aa-53f89847e1f4', 'TACHE_ASSIGNATION', 'salma', 1, 17),
(383, '2025-11-15 17:25:28.000000', b'0', 'Vous avez été ajouté à la tâche : LOdyssée invisible#38d9f2a8-19a0-4b15-90aa-53f89847e1f4', 'TACHE_ASSIGNATION', 'sameh', 1, 17),
(384, '2025-11-15 17:54:21.000000', b'0', 'Vous avez été ajouté à la tâche : Pourquoi je mens ?#f8d171d4-ad19-4f3d-a4fe-cb1241a2f627', 'TACHE_ASSIGNATION', 'salma', 1, 18),
(385, '2025-11-15 17:54:21.000000', b'0', 'Vous avez été ajouté à la tâche : Pourquoi je mens ?#f8d171d4-ad19-4f3d-a4fe-cb1241a2f627', 'TACHE_ASSIGNATION', 'sameh', 1, 18),
(386, '2025-11-15 17:55:24.000000', b'0', 'Vous avez été ajouté à la tâche : LOdeur du sanglier#ac808823-bd05-4472-938f-61f96c26f34b', 'TACHE_ASSIGNATION', 'salma', 1, 19),
(387, '2025-11-15 17:55:24.000000', b'0', 'Vous avez été ajouté à la tâche : LOdeur du sanglier#ac808823-bd05-4472-938f-61f96c26f34b', 'TACHE_ASSIGNATION', 'sameh', 1, 19),
(388, '2025-11-15 17:56:33.000000', b'0', 'Vous avez été ajouté à la tâche : Pourquoi je mens ?#57cefeae-1cd2-4aba-a72e-575a7a869593', 'TACHE_ASSIGNATION', 'salma', 1, 20),
(389, '2025-11-15 17:56:33.000000', b'0', 'Vous avez été ajouté à la tâche : Pourquoi je mens ?#57cefeae-1cd2-4aba-a72e-575a7a869593', 'TACHE_ASSIGNATION', 'sameh', 1, 20),
(390, '2025-11-15 17:57:41.000000', b'0', 'Vous avez été ajouté à la tâche : La Discipline des orphelins#02e907ad-2149-4b59-96fd-597318a7084c', 'TACHE_ASSIGNATION', 'salma', 1, 21),
(391, '2025-11-15 17:57:41.000000', b'0', 'Vous avez été ajouté à la tâche : La Discipline des orphelins#02e907ad-2149-4b59-96fd-597318a7084c', 'TACHE_ASSIGNATION', 'sameh', 1, 21),
(392, '2025-11-15 17:58:48.000000', b'0', 'Vous avez été ajouté à la tâche : La Promise du voyeur#5edf42c3-4479-4b6d-8ff8-d1959d9627a8', 'TACHE_ASSIGNATION', 'salma', 1, 22),
(393, '2025-11-15 17:58:48.000000', b'0', 'Vous avez été ajouté à la tâche : La Promise du voyeur#5edf42c3-4479-4b6d-8ff8-d1959d9627a8', 'TACHE_ASSIGNATION', 'sameh', 1, 22),
(394, '2025-11-15 17:59:51.000000', b'0', 'Vous avez été ajouté à la tâche : LOdyssée invisible#54c54b7c-a6bf-423a-b961-49878a4c89c2', 'TACHE_ASSIGNATION', 'salma', 1, 23),
(395, '2025-11-15 17:59:51.000000', b'0', 'Vous avez été ajouté à la tâche : LOdyssée invisible#54c54b7c-a6bf-423a-b961-49878a4c89c2', 'TACHE_ASSIGNATION', 'sameh', 1, 23),
(396, '2025-11-15 18:00:59.000000', b'0', 'Vous avez été ajouté à la tâche : Pourquoi je mens ?#6c6f50dc-2fce-4b96-9d77-910b0ea0ce62', 'TACHE_ASSIGNATION', 'salma', 1, 24),
(397, '2025-11-15 18:00:59.000000', b'0', 'Vous avez été ajouté à la tâche : Pourquoi je mens ?#6c6f50dc-2fce-4b96-9d77-910b0ea0ce62', 'TACHE_ASSIGNATION', 'sameh', 1, 24),
(398, '2025-11-15 18:02:06.000000', b'0', 'Vous avez été ajouté à la tâche : La Peau des savants#aac7dc32-5eee-4283-ac3e-cb2b665baad7', 'TACHE_ASSIGNATION', 'salma', 1, 25),
(399, '2025-11-15 18:02:06.000000', b'0', 'Vous avez été ajouté à la tâche : La Peau des savants#aac7dc32-5eee-4283-ac3e-cb2b665baad7', 'TACHE_ASSIGNATION', 'sameh', 1, 25),
(400, '2025-11-15 18:03:15.000000', b'0', 'Vous avez été ajouté à la tâche : Le Siècle de la rue voisine#8c5d7a76-9b52-4435-8d11-a73869a15253', 'TACHE_ASSIGNATION', 'salma', 1, 26),
(401, '2025-11-15 18:03:15.000000', b'0', 'Vous avez été ajouté à la tâche : Le Siècle de la rue voisine#8c5d7a76-9b52-4435-8d11-a73869a15253', 'TACHE_ASSIGNATION', 'sameh', 1, 26),
(402, '2025-11-15 18:04:28.000000', b'0', 'Vous avez été ajouté à la tâche : LOdeur du sanglier#03846606-a1ff-429c-a051-9302ee929f19', 'TACHE_ASSIGNATION', 'salma', 1, 27),
(403, '2025-11-15 18:04:28.000000', b'0', 'Vous avez été ajouté à la tâche : LOdeur du sanglier#03846606-a1ff-429c-a051-9302ee929f19', 'TACHE_ASSIGNATION', 'sameh', 1, 27),
(404, '2025-11-15 18:05:36.000000', b'0', 'Vous avez été ajouté à la tâche : Le Couloir de tous les mépris#be939bff-8d73-4d1f-8cbf-9cfb048d51e7', 'TACHE_ASSIGNATION', 'salma', 1, 28),
(405, '2025-11-15 18:05:36.000000', b'0', 'Vous avez été ajouté à la tâche : Le Couloir de tous les mépris#be939bff-8d73-4d1f-8cbf-9cfb048d51e7', 'TACHE_ASSIGNATION', 'sameh', 1, 28),
(406, '2025-11-15 18:06:47.000000', b'0', 'Vous avez été ajouté à la tâche : Le Désir des femmes fortes#44824b88-d5dc-42e5-af9e-85cd69866278', 'TACHE_ASSIGNATION', 'salma', 1, 29),
(407, '2025-11-15 18:06:47.000000', b'0', 'Vous avez été ajouté à la tâche : Le Désir des femmes fortes#44824b88-d5dc-42e5-af9e-85cd69866278', 'TACHE_ASSIGNATION', 'sameh', 1, 29);

-- --------------------------------------------------------

--
-- Structure de la table `projets`
--

CREATE TABLE `projets` (
  `numero_projet` bigint(20) NOT NULL,
  `archived` bit(1) NOT NULL,
  `date_debut` date DEFAULT NULL,
  `date_fin` date DEFAULT NULL,
  `deadline_depassee_notifiee` bit(1) DEFAULT NULL,
  `deadline_proche_notifiee` bit(1) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `status` enum('EN_ATTENTE','EN_COURS','TERMINE','ANNULE') DEFAULT NULL,
  `titre` varchar(100) NOT NULL,
  `chef_projet_matricule` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `projets`
--

INSERT INTO `projets` (`numero_projet`, `archived`, `date_debut`, `date_fin`, `deadline_depassee_notifiee`, `deadline_proche_notifiee`, `description`, `status`, `titre`, `chef_projet_matricule`) VALUES
(1, b'0', '2026-01-01', '2030-01-01', b'0', b'0', 'projet pour tester les taches', 'EN_COURS', 'Project Management', 'ala'),
(2, b'0', '2025-12-03', '2026-01-06', b'0', b'0', 'Excepturi in qui dicta corporis nulla qui eum numquam sit tenetur perferendis.', 'EN_COURS', 'La Promise du voyeur#fb68c983-30fd-492c-9c69-27aa0493ba7f', 'ala'),
(3, b'0', '2025-12-05', '2026-02-14', b'0', b'0', 'Dolor ipsum et eligendi ipsum dolore optio sit ut.', 'EN_COURS', 'La Promise du voyeur#67e2a35b-ee95-4122-a653-32c77f561f1c', 'ala'),
(4, b'0', '2025-12-13', '2026-06-08', b'0', b'0', 'Quia dolorum ea est rerum illum explicabo temporibus quae ea.', 'EN_COURS', 'La Soumission comme passion#2aa841bc-d617-459b-802c-e84e3a155e3d', 'ala'),
(5, b'0', '2025-11-28', '2026-04-15', b'0', b'0', 'Quaerat et debitis inventore quia ut consequatur fugit non iure.', 'EN_COURS', 'Le Désir des femmes fortes#22a32114-6558-40ec-af5c-a4e74ca8d2f6', 'ala'),
(6, b'0', '2025-12-11', '2026-05-09', b'0', b'0', 'Ut labore facere deleniti repellendus dignissimos quas culpa cum dolorem.', 'EN_COURS', 'Le Désir des femmes fortes#c61438be-11e3-4082-ba64-35de37cec5b6', 'ala'),
(7, b'0', '2025-11-19', '2026-05-10', b'0', b'0', 'Eaque modi voluptas doloribus sit illo consequatur qui id ea rem ullam quas odit.', 'EN_COURS', 'LOdyssée invisible#3d591cd9-5aa4-43de-96ed-5c3195b8bab6', 'ala'),
(8, b'0', '2025-12-04', '2026-02-11', b'0', b'0', 'Quia libero molestiae reiciendis labore dolores voluptate dignissimos quibusdam reprehenderit alias.', 'EN_COURS', 'La', 'ala'),
(9, b'0', '2025-11-29', '2025-12-01', b'0', b'0', 'Quaerat et placeat quae harum rerum asperiores voluptas eum.', 'EN_COURS', 'Pourquoi je mens ?#e2e0f3df-04a7-49f6-828f-81c349f23614', 'ala'),
(10, b'0', '2025-12-05', '2025-12-24', b'0', b'0', 'Voluptatem voluptatum ipsam molestiae amet qui amet nihil vitae.', 'EN_COURS', 'Le Désir des femmes fortes#57eeecd5-80d2-453b-8732-3c385694c101', 'ala'),
(11, b'0', '2025-11-27', '2026-04-20', b'0', b'0', 'Corrupti beatae exercitationem esse consequatur est neque odio ratione nam nemo fuga sit.', 'EN_COURS', 'La Soumission comme passion#a86d0cdf-08a9-4d79-9d10-840bd0a46985', 'ala'),
(12, b'0', '2025-12-08', '2026-02-13', b'0', b'0', 'Voluptatem vel voluptatem omnis et sequi assumenda voluptatem ut nesciunt.', 'EN_COURS', 'La Peau des savants#8ced2f95-8a97-407b-8361-9a75076a037d', 'ala'),
(13, b'0', '2025-11-21', '2026-01-31', b'0', b'0', 'Et quisquam et corporis sed rem omnis vitae mollitia ipsa qui dolores vitae et.', 'EN_COURS', 'La Discipline des orphelins#a461681d-7b71-4f7c-9577-ef0a2847f170', 'ala'),
(14, b'0', '2025-11-15', '2026-03-28', b'0', b'0', 'Et necessitatibus qui doloremque voluptas porro et officia modi et sint.', 'EN_COURS', 'La progéniture du mal#5b836bd5-a589-4af4-b19d-56f028c3998d', 'ala'),
(15, b'0', '2025-12-14', '2026-05-13', b'0', b'0', 'Vel voluptatem ut numquam quia sequi officia non nemo id nemo.', 'EN_COURS', 'La Soumission comme passion#930cab62-a60e-472f-b061-8d99bdb7f1d8', 'ala'),
(16, b'0', '2025-12-11', '2026-04-07', b'0', b'0', 'Assumenda deleniti aut ullam rerum asperiores vel iure natus natus ea sit error.', 'EN_COURS', 'Le Désir des femmes fortes#daf4e655-65e9-4285-909b-420f7ea19e6b', 'ala'),
(17, b'0', '2025-11-17', '2025-12-08', b'0', b'0', 'Nesciunt ipsam adipisci et inventore rerum fugit possimus a ipsam praesentium in dolores.', 'EN_COURS', 'LOdyssée invisible#85ebc8e0-eacb-4ec7-a3cc-93806a7c4b6b', 'ala'),
(18, b'0', '2025-11-15', '2025-12-16', b'0', b'0', 'Minus est officia aut quibusdam aut consequatur nostrum iste minus itaque doloremque velit.', 'EN_COURS', 'La Soumission comme passion#897f852e-dac7-4e1e-9f9b-9d3d7236fc91', 'ala'),
(19, b'0', '2025-12-14', '2026-03-01', b'0', b'0', 'Enim neque quo laudantium sit consequatur nulla aperiam similique ut.', 'EN_COURS', 'La Promise du voyeur#6084fbde-3b26-4491-8f34-bbc615681100', 'ala'),
(20, b'0', '2025-12-14', '2026-05-07', b'0', b'0', 'Dignissimos consequatur et qui voluptatem eaque et voluptatem ea et sapiente praesentium perferendis.', 'EN_COURS', 'La Peau des savants#6534644a-4cb5-453a-abab-78bb4976fd35', 'ala'),
(21, b'0', '2025-11-24', '2025-11-27', b'0', b'0', 'Et quisquam illo ipsa temporibus culpa est quia eum repudiandae vitae.', 'EN_COURS', 'La Discipline des orphelins#523ced5b-073f-4563-98f3-5946ff15a8f8', 'ala'),
(22, b'0', '2025-11-20', '2026-03-26', b'0', b'0', 'Quia sit error sunt et fuga temporibus reiciendis fugiat libero et assumenda dignissimos sed.', 'EN_ATTENTE', 'La Peau des savants#899fc1f4-be32-46eb-a541-7ac5cdbfbe67', 'ala'),
(23, b'0', '2025-12-09', '2025-12-23', b'0', b'0', 'Quas est suscipit doloremque unde quia repudiandae voluptas incidunt molestias earum deserunt.', 'EN_COURS', 'La Promise du voyeur#b3af799a-d68a-4873-80c9-5a4e5a83bf74', 'ala'),
(24, b'0', '2025-12-03', '2026-03-10', b'0', b'0', 'Architecto sit sit voluptate ducimus qui quis nisi aut et aspernatur qui reprehenderit consequatur.', 'EN_COURS', 'La Discipline des orphelins#7122dcef-ebbb-4660-9138-a678284a14a5', 'ala'),
(25, b'0', '2025-11-25', '2026-05-01', b'0', b'0', 'Quas dolores voluptatem et itaque dolorem et voluptate optio et laudantium nihil non hic.', 'ANNULE', 'La progéniture du mal#9755e2b7-3877-4b06-aed1-753bec681ca9', 'ala'),
(26, b'0', '2025-12-07', '2026-05-30', b'0', b'0', 'Sunt id ut eum minus unde quis voluptatem veniam sapiente sapiente ad.', 'ANNULE', 'Le Siècle de la rue voisine#4945a6b1-59a2-4a14-a2e0-f1a2d141601e', 'ala'),
(27, b'0', '2025-12-13', '2026-05-10', b'0', b'0', 'Qui qui natus sed et sunt et tempora aut a quia fugit est.', 'EN_ATTENTE', 'La Peau des savants#522b6759-d814-4566-b2b1-7b5c28779e98', 'ala'),
(28, b'0', '2025-11-17', '2025-12-04', b'0', b'0', 'Voluptatem asperiores voluptates blanditiis recusandae in provident rem error officiis officia.', 'ANNULE', 'La Promise du voyeur#1b38537a-b835-4e83-9aed-cf5158d4a4e3', 'ala'),
(29, b'0', '2025-12-02', '2026-02-14', b'0', b'0', 'Voluptatibus ut sint mollitia incidunt corrupti sit et ex ut voluptas odit.', 'EN_COURS', 'LOdeur du sanglier#07fc829e-22bc-49c0-9b03-ef5f42f27a23', 'ala'),
(30, b'0', '2025-12-01', '2026-03-07', b'0', b'0', 'Fugiat voluptas nihil beatae quibusdam enim corporis corporis voluptas repellendus.', 'EN_COURS', 'Le Désir des femmes fortes#6fd88b51-f0ea-4408-9bbd-16f7f7a704c1', 'ala'),
(31, b'0', '2025-12-02', '2025-12-07', b'0', b'0', 'Pariatur et tenetur omnis tempora sed pariatur repellat natus ab.', 'EN_COURS', 'Le Siècle de la rue voisine#c861104a-cf4c-4505-9d7d-69d2403b9c80', 'ala'),
(32, b'0', '2025-11-29', '2026-01-14', b'0', b'0', 'Ratione nesciunt temporibus ut sed repellendus possimus illo cupiditate non facere qui.', 'EN_COURS', 'La Discipline des orphelins#eb782f08-775c-4fc2-afb5-69dae431389f', 'ala'),
(33, b'0', '2025-11-27', '2026-04-05', b'0', b'0', 'Unde qui ipsum eos minima minus velit incidunt animi ut ipsum fuga.', 'EN_COURS', 'Le Désir des femmes fortes#c2636e30-2335-4c4f-b452-b78fbd94f2cc', 'ala'),
(34, b'0', '2025-11-15', '2026-01-17', b'0', b'0', 'Voluptatem et illo ut commodi dolores aut ut voluptatem alias in optio nobis veniam.', 'EN_COURS', 'La progéniture du mal#dbae7df2-7bd7-45e6-8cb4-1d4fea434b46', 'ala'),
(35, b'0', '2025-12-03', '2026-02-24', b'0', b'0', 'Omnis non officiis odit culpa rerum rerum nobis et alias.', 'EN_COURS', 'LOdeur du sanglier#cf70e0b9-66e8-47c6-86b4-ed51e3b1f20a', 'ala'),
(36, b'0', '2025-11-28', '2026-04-26', b'0', b'0', 'Nobis aut saepe autem facilis aliquam ratione quis magnam animi ea.', 'EN_COURS', 'La progéniture du mal#ba109426-051b-4dfa-858a-ed849d57f0b6', 'ala'),
(37, b'0', '2025-11-19', '2026-01-28', b'0', b'0', 'Asperiores et esse id dolores quas non at iure.', 'EN_COURS', 'Le Siècle de la rue voisine#c453dd7e-06fd-4b29-8217-34367698271a', 'ala'),
(38, b'0', '2025-11-20', '2026-01-12', b'0', b'0', 'Voluptas perspiciatis quia molestiae et consequatur aperiam qui necessitatibus.', 'EN_ATTENTE', 'Le Couloir de tous les mépris#86fdf62e-0dec-48a8-8d7b-55c5b88bfb5c', 'ala'),
(39, b'0', '2025-12-10', '2026-01-20', b'0', b'0', 'Dolorum hic aperiam facilis vitae ipsum rerum adipisci architecto enim molestiae quia dignissimos.', 'ANNULE', 'Le Siècle de la rue voisine#cdfc0ea3-5fe4-4d0e-9615-2e2afb02bad4', 'ala'),
(40, b'0', '2025-12-08', '2026-03-22', b'0', b'0', 'Omnis quae et nihil cupiditate similique magnam molestias temporibus.', 'EN_COURS', 'LOdyssée invisible#1264156d-8ec8-4357-bfcf-89bee0471a75', 'ala'),
(41, b'0', '2025-12-03', '2026-04-21', b'0', b'0', 'Velit tempora voluptatem deserunt et vel ut sit hic.', 'ANNULE', 'Le Couloir de tous les mépris#f8c9d16e-a16a-4ee7-bc11-723bdf3dd7ed', 'ala'),
(42, b'0', '2025-12-01', '2026-05-06', b'0', b'0', 'Vero consectetur officiis deleniti quaerat necessitatibus eius quisquam et voluptatem est.', 'ANNULE', 'Le Désir des femmes fortes#b77d6eff-d3eb-4d22-aa75-92e2f6bd4062', 'ala'),
(43, b'1', '2025-11-16', '2026-03-03', b'0', b'0', 'Deserunt pariatur in nemo nesciunt voluptatem veritatis optio et esse.', 'EN_COURS', 'Le Désir des femmes fortes#374ead0f-44e5-4322-9b90-29a03a3a6ca9', 'ala'),
(44, b'0', '2025-11-17', '2026-02-06', b'0', b'0', 'Tempora molestias omnis impedit vero temporibus voluptas sunt nostrum.', 'EN_COURS', 'LOdeur du sanglier#69135a07-fb4a-4ead-9f83-aa74fbe8a43e', 'ala'),
(45, b'0', '2025-12-03', '2026-05-15', b'0', b'0', 'Beatae enim nemo tempore debitis quidem nostrum dignissimos dolor dicta.', 'EN_COURS', 'La Promise du voyeur#701f7456-c7a0-4c5a-95b2-51a2cedcc6d0', 'ala'),
(46, b'0', '2025-12-05', '2026-01-07', b'0', b'0', 'Nemo excepturi amet quae porro dolores quia nulla natus expedita.', 'EN_ATTENTE', 'La Promise du voyeur#a2bf0d73-2872-4b40-8142-0fca752d2696', 'ala'),
(47, b'0', '2025-12-06', '2026-05-19', b'0', b'0', 'Dicta placeat sed esse corporis et nostrum sapiente ut.', 'ANNULE', 'La Promise du voyeur#5cbdfd0c-f953-40d3-92ae-9e112cea1d72', 'ala'),
(48, b'0', '2025-12-14', '2026-05-07', b'0', b'0', 'Ducimus velit quisquam excepturi dolores itaque minima cumque illum.', 'EN_COURS', 'LOdyssée invisible#66ea3148-a7ad-4f4f-811f-d0dfa8143617', 'ala'),
(49, b'0', '2025-11-22', '2025-12-02', b'0', b'0', 'Et et quia voluptas sint ut consequatur architecto sunt molestias veniam.', 'EN_COURS', 'Le Désir des femmes fortes#f2a25a96-cfd2-4313-a885-8c532eab46aa', 'ala'),
(50, b'0', '2025-11-28', '2026-01-13', b'0', b'0', 'Quos suscipit unde voluptates delectus esse aperiam aut non laudantium non.', 'EN_COURS', 'Le Désir des femmes fortes#cdb4ec57-c79d-48e5-bd89-7f3b621753f9', 'ala'),
(51, b'0', '2025-12-11', '2026-01-23', b'0', b'0', 'Officiis nihil aliquid ut sequi dolorum facilis voluptas adipisci aperiam ducimus accusantium.', 'EN_COURS', 'La Peau des savants#d9e1a17a-8f67-4617-b7fd-71d635f1b8c2', 'ala'),
(52, b'0', '2025-11-16', '2026-04-05', b'0', b'0', 'Fugit ullam impedit accusamus voluptas ullam et amet velit sed delectus mollitia.', 'EN_COURS', 'Pourquoi je mens ?#89f53cdd-23a5-47c9-a11e-ba3f46ab5f84', 'ala'),
(53, b'0', '2025-12-06', '2026-02-28', b'0', b'0', 'Commodi ut fugiat et aspernatur nulla similique quos debitis magni consequatur.', 'EN_COURS', 'Pourquoi je mens ?#f66b0d3d-915a-4a05-be17-47a1a009cf0a', 'ala'),
(54, b'0', '2025-11-15', '2026-01-19', b'0', b'0', 'Perspiciatis veritatis similique omnis ratione vel eligendi hic ullam ut blanditiis sapiente.', 'EN_COURS', 'LOdeur du sanglier#a5098c4d-a3d7-4694-9b8d-456d05f284c0', 'ala'),
(55, b'0', '2025-11-17', '2025-12-10', b'0', b'0', 'Necessitatibus nesciunt quo quis dolore quod maiores et unde et.', 'EN_COURS', 'La progéniture du mal#a2b1a1a5-8a49-4d15-aeb2-908bf5286421', 'ala'),
(56, b'0', '2025-12-07', '2026-01-15', b'0', b'0', 'Maxime eveniet aliquid vel odio tempore commodi aliquam itaque rerum et neque rem quia.', 'EN_COURS', 'Le Siècle de la rue voisine#7403ef94-44b0-4974-86c4-8c1aad30d0c6', 'ala'),
(57, b'1', '2025-11-26', '2026-04-25', b'0', b'0', 'Et fugit voluptate beatae cumque id ratione quis et numquam et quo sit.', 'EN_COURS', 'La progéniture du mal#80dda495-f412-4982-af4b-976a97cfd6f6', 'ala'),
(58, b'0', '2025-11-27', '2026-05-24', b'0', b'0', 'Autem id aut facilis est et reprehenderit voluptas error.', 'EN_COURS', 'La Peau des savants#856788f1-1d41-43ec-b53b-2bc9a23752ab', 'ala'),
(59, b'0', '2025-11-23', '2025-12-13', b'0', b'0', 'Aut libero culpa aspernatur labore enim doloribus impedit aut perferendis sapiente vel facilis.', 'EN_COURS', 'La Soumission comme passion#da04d2d6-bfa2-41ea-af8e-7ad78d470e0a', 'ala'),
(60, b'0', '2025-12-14', '2026-02-09', b'0', b'0', 'Facilis rerum et eum voluptas iusto omnis et sequi.', 'EN_ATTENTE', 'La Promise du voyeur#d0fb0d80-504b-46b7-b167-251bdea1e273', 'ala'),
(61, b'0', '2025-11-28', '2026-05-04', b'0', b'0', 'Consequatur earum alias ut sint dolorem eum vitae eos saepe et exercitationem magnam.', 'ANNULE', 'La Discipline des orphelins#b2729164-003e-4f45-9555-d606f71997a3', 'ala'),
(62, b'0', '2025-11-24', '2026-02-08', b'0', b'0', 'Sunt cum quae a nobis voluptatem ex nemo praesentium neque molestiae.', 'EN_COURS', 'La Discipline des orphelins#f8e6c723-ae9b-442a-a6aa-32d0167e514c', 'ala'),
(63, b'0', '2025-12-06', '2026-06-02', b'0', b'0', 'Omnis repellendus velit qui sequi qui pariatur nisi molestiae.', 'EN_COURS', 'Pourquoi je mens ?#d3469b60-10b4-4ad7-bd33-f951c6eca18d', 'ala'),
(64, b'0', '2025-12-08', '2025-12-09', b'0', b'0', 'Eaque animi est at alias et deleniti ipsa repellendus sint occaecati quia aut.', 'EN_COURS', 'LOdeur du sanglier#24a6b07b-0674-4cc5-970f-371546157305', 'ala'),
(65, b'0', '2025-12-13', '2026-03-12', b'0', b'0', 'Quisquam maiores sint incidunt rerum porro aut provident in voluptatem.', 'EN_COURS', 'Le Couloir de tous les mépris#f75bd573-3254-4df8-afe2-f38c9b1a4b10', 'ala'),
(66, b'0', '2025-12-14', '2026-02-21', b'0', b'0', 'Voluptatibus sint officia quisquam rerum voluptatem consequatur id saepe est in saepe et.', 'EN_COURS', 'LOdeur du sanglier#e1e145a7-de08-46d3-8bc7-610cf6520c37', 'ala'),
(67, b'0', '2025-11-27', '2026-03-23', b'0', b'0', 'Doloribus esse sequi impedit totam culpa vel porro a qui sint velit.', 'EN_COURS', 'LOdyssée invisible#987202fa-440c-48d0-93a9-70dd1ce3ab10', 'ala'),
(68, b'0', '2025-11-22', '2026-05-20', b'0', b'0', 'Doloremque suscipit maiores harum consequatur est dolorum qui eos quibusdam consequuntur occaecati.', 'EN_COURS', 'LOdyssée invisible#839e1031-fe62-47c8-a43c-e24e8e8ff752', 'ala'),
(69, b'0', '2025-12-01', '2026-02-18', b'0', b'0', 'Nihil itaque est deleniti voluptate illo eum molestiae rerum.', 'EN_COURS', 'Pourquoi je mens ?#dbed84b7-6fb1-4639-9e38-8565deb1df0c', 'ala'),
(70, b'0', '2025-12-05', '2026-04-21', b'0', b'0', 'Non laboriosam praesentium quo perspiciatis exercitationem molestiae officia ipsum autem.', 'EN_COURS', 'La Discipline des orphelins#ac22d00b-5431-4da1-9ecb-acc81d6f1437', 'ala');

-- --------------------------------------------------------

--
-- Structure de la table `projet_fichiers`
--

CREATE TABLE `projet_fichiers` (
  `projet_id` bigint(20) NOT NULL,
  `fichier` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `projet_fichiers`
--

INSERT INTO `projet_fichiers` (`projet_id`, `fichier`) VALUES
(1, '/uploads/files/438c3b3d-1bf2-4ae1-8545-e0002342babe_ajouter_objectif_sequence.png'),
(1, '/uploads/files/ff121744-b8bc-457e-92fb-ee6bbbf5e7a4_ajouter_patient_sequence.png'),
(1, '/uploads/files/687d7d8b-3ac6-4398-9266-5c7a65b745b6_ajouter_programme_sequence.png'),
(2, '/uploads/files/c963ed6f-ef39-4717-943c-111e543bb99d_image 1.jpeg'),
(2, '/uploads/files/b284fa08-8f24-4415-9b24-3dd42e2e32a2_BDD.pdf'),
(2, '/uploads/files/54c26d9e-5c69-4f32-8940-92947e5680ba_cas de tests.xlsx'),
(3, '/uploads/files/401587db-5c7f-490c-b5de-a3fb99e4c0b3_image 1.jpeg'),
(3, '/uploads/files/e41f04bf-82f9-462d-aee0-e3528198750c_BDD.pdf'),
(3, '/uploads/files/4ae8fa40-7374-4417-8fe3-ec211b18b8c2_cas de tests.xlsx'),
(4, '/uploads/files/da6ee028-5f9d-4de9-9385-2e9ca420f59f_image 1.jpeg'),
(4, '/uploads/files/104d746a-2ee1-46c4-b30d-6b54548f3ed9_BDD.pdf'),
(4, '/uploads/files/f66ba38f-1f61-430f-ac0b-2085cd9d3f70_cas de tests.xlsx'),
(5, '/uploads/files/66b4102d-9866-4467-a185-793dfab259d4_image 1.jpeg'),
(5, '/uploads/files/5e18e113-c730-42f7-b140-89372ad9c1d9_BDD.pdf'),
(5, '/uploads/files/725d4da5-c62d-4c70-b1e3-6de5ccb2532c_cas de tests.xlsx'),
(6, '/uploads/files/48c34a06-5e40-48f8-8c5f-58c491e1bdbb_image 1.jpeg'),
(6, '/uploads/files/aebf835b-b003-4a83-9bce-a3e643249a84_BDD.pdf'),
(6, '/uploads/files/55d98766-5593-4fe4-9843-ca0a717f6e8f_cas de tests.xlsx'),
(7, '/uploads/files/ee6ad957-9cfc-44ae-ac8a-90a72c77aedc_image 1.jpeg'),
(7, '/uploads/files/b2e63556-d6bd-48c4-9969-a9df7d855b80_BDD.pdf'),
(7, '/uploads/files/f3d3fa3b-6820-43a2-86e8-5b35d6232c4f_cas de tests.xlsx'),
(8, '/uploads/files/bc81a683-2336-4271-ac5e-191faaaa6516_image 1.jpeg'),
(8, '/uploads/files/7df30225-ddfb-49ec-a012-ea30514b57fc_BDD.pdf'),
(8, '/uploads/files/dbba30b1-3067-4d88-84cc-87ce1ff4415e_cas de tests.xlsx'),
(9, '/uploads/files/b62d3219-3a6c-48f3-b76f-2216de9b2224_image 1.jpeg'),
(9, '/uploads/files/1375d62b-5c10-4287-ae3a-e99c98f91f75_BDD.pdf'),
(9, '/uploads/files/d16390f1-68fa-4a04-8a3a-2e4a7c548b84_cas de tests.xlsx'),
(10, '/uploads/files/0fe6fbc5-9e0c-47c2-bb88-5bbb68239c28_image 1.jpeg'),
(10, '/uploads/files/12e2aa35-34b9-4e1c-94be-bbb1ff6f4b2c_BDD.pdf'),
(10, '/uploads/files/f9fda59b-13c1-486e-8c5a-32d1fcea6592_cas de tests.xlsx'),
(11, '/uploads/files/14d827c4-1897-4c68-b63d-7fa033e28901_image 1.jpeg'),
(11, '/uploads/files/2018b1aa-08de-4c09-9eab-4dec43918c92_BDD.pdf'),
(11, '/uploads/files/3aed6cbb-8c3f-4ff5-952f-0a1d6f8ce1ce_cas de tests.xlsx'),
(12, '/uploads/files/2748f95f-e1f5-4b13-a733-58a118030762_image 1.jpeg'),
(12, '/uploads/files/adbd04da-517b-46d0-955a-9198b8c86324_BDD.pdf'),
(12, '/uploads/files/d8f63c46-1613-4379-b2d1-36b24390a13f_cas de tests.xlsx'),
(13, '/uploads/files/87824c1c-f3e8-430b-8497-63c8b6e4c1f0_image 1.jpeg'),
(13, '/uploads/files/61c13346-41e6-4a58-a515-26608e7b5ab8_BDD.pdf'),
(13, '/uploads/files/5ccd8d66-f199-4c1c-83d8-ee4242080f6e_cas de tests.xlsx'),
(14, '/uploads/files/ace704fc-6a7e-4190-9028-9e3c4107bb1a_image 1.jpeg'),
(14, '/uploads/files/16a2dfdc-36e1-40ec-a99f-28ca6981f6c6_BDD.pdf'),
(14, '/uploads/files/bb043cdd-1d37-4320-bed3-fe2cefff90fc_cas de tests.xlsx'),
(15, '/uploads/files/ffdbb7b0-269c-4375-8828-39624bb8f64f_image 1.jpeg'),
(15, '/uploads/files/ca2bd19e-dc43-451a-a37c-8903f0b1ccdf_BDD.pdf'),
(15, '/uploads/files/28973f5e-1ab0-4263-b263-e60a38d29032_cas de tests.xlsx'),
(16, '/uploads/files/256864ef-5fb5-45aa-811a-d1d32dbcb947_image 1.jpeg'),
(16, '/uploads/files/d8705dec-0718-489a-ac65-2513cb5bd6a5_BDD.pdf'),
(16, '/uploads/files/41b92108-238e-4f6a-a897-b5f046ccdd86_cas de tests.xlsx'),
(17, '/uploads/files/a8cafe06-4552-492a-bfc4-0bf41e9f1d75_image 1.jpeg'),
(17, '/uploads/files/4ba5777f-8317-4e9a-8de2-17394d39ba73_BDD.pdf'),
(17, '/uploads/files/4a1560e7-4799-4423-a88d-59ad51139ed5_cas de tests.xlsx'),
(18, '/uploads/files/baec474e-56b2-4a95-b522-e526c70289f5_image 1.jpeg'),
(18, '/uploads/files/ec4971b8-fc2d-40ff-b932-408025fd5854_BDD.pdf'),
(18, '/uploads/files/bfa2a4fe-0812-48ca-8e3f-b0885f0e7f65_cas de tests.xlsx'),
(19, '/uploads/files/ac9a88f0-a6b9-4e65-9d22-d906aee8a6e7_image 1.jpeg'),
(19, '/uploads/files/5b58e4fe-db54-43b0-b390-3498a57886d4_BDD.pdf'),
(19, '/uploads/files/647fa990-c780-4395-a277-c74ec3905c03_cas de tests.xlsx'),
(20, '/uploads/files/5b47d2a7-630c-4121-9427-e27f72f3ee69_image 1.jpeg'),
(20, '/uploads/files/c3530790-0f4a-4e6c-b6da-cd2ebc85d7ba_BDD.pdf'),
(20, '/uploads/files/384ab510-1151-4f6e-8e83-59535f8392ea_cas de tests.xlsx'),
(21, '/uploads/files/6d8157be-d93f-43c9-9351-29a4516c5ff7_image 1.jpeg'),
(21, '/uploads/files/01b06cb7-9744-4ade-b299-dbdba1585a6c_BDD.pdf'),
(21, '/uploads/files/ecd3d369-14c3-4e52-b628-07b307a937a6_cas de tests.xlsx'),
(22, '/uploads/files/5a6c3747-1bef-4664-a27f-d94d1c7cbd63_cas de tests.xlsx'),
(22, '/uploads/files/c0bfc968-1c4c-4370-bd0f-0fa622ebbe60_image 1.jpeg'),
(22, '/uploads/files/7960fe61-2d23-4e17-8d4d-d7bdd2d64c9f_BDD.pdf'),
(23, '/uploads/files/f24485a3-a13b-4bcc-a7f9-9121f93f15c3_image 1.jpeg'),
(23, '/uploads/files/a660dba1-b004-43c6-9e45-99cbb32611e2_BDD.pdf'),
(23, '/uploads/files/338aeee7-c431-42b7-8243-0b7fd0f97f3c_cas de tests.xlsx'),
(24, '/uploads/files/6dcf0430-dfdd-4817-943b-419cdaae5397_image 1.jpeg'),
(24, '/uploads/files/80b9f270-f847-44b2-9d18-fe4db1a5bd92_BDD.pdf'),
(24, '/uploads/files/f3257f52-8c4b-4dce-b59e-811ffe8d482e_cas de tests.xlsx'),
(25, '/uploads/files/8b8a31fc-15c3-4dc5-9d70-714b2de7ad59_image 1.jpeg'),
(25, '/uploads/files/b01bc223-dc70-4fb3-ad07-0259e7292ebb_BDD.pdf'),
(25, '/uploads/files/9574c94d-80a8-48fb-963e-2bd637138612_cas de tests.xlsx'),
(26, '/uploads/files/035194c2-7cc5-4802-8165-1984baf2e5b3_image 1.jpeg'),
(26, '/uploads/files/e3b3270c-65e6-41fe-af43-6e0a08c9be04_BDD.pdf'),
(26, '/uploads/files/6f3a5a1b-4d0b-435b-b7b5-3f18d100cf94_cas de tests.xlsx'),
(27, '/uploads/files/7d476449-f440-4cd7-a604-982a1671c31d_cas de tests.xlsx'),
(27, '/uploads/files/34127a2a-8971-4dbc-8075-ff5cb8ffe196_image 1.jpeg'),
(27, '/uploads/files/a456ec51-c54b-4635-8eed-057c044b844f_BDD.pdf'),
(28, '/uploads/files/54598064-a1e6-40d9-a488-a66912a9bb5f_image 1.jpeg'),
(28, '/uploads/files/8d1df12a-0684-4be9-ade2-159485142f37_BDD.pdf'),
(28, '/uploads/files/ab2ad4a0-724a-4dba-a777-6d95063dce02_cas de tests.xlsx'),
(29, '/uploads/files/69a7d041-59f8-4ff1-9c4e-6ca4667cce23_image 1.jpeg'),
(29, '/uploads/files/2349337b-e7a0-40d3-aa27-cd354bdb22b4_BDD.pdf'),
(29, '/uploads/files/6667615f-3a3e-4f6e-a98a-1834c9d0faf8_cas de tests.xlsx'),
(30, '/uploads/files/398a0e6f-333a-4428-9101-15c70c17275e_image 1.jpeg'),
(30, '/uploads/files/fc82ec4d-f2f6-4ba8-9d35-b25584555fed_BDD.pdf'),
(30, '/uploads/files/95a26fbf-a85e-4383-b85c-0a4229ab5f08_cas de tests.xlsx'),
(31, '/uploads/files/e060fd5d-9528-4a11-96ba-6d2e7dd285c8_image 1.jpeg'),
(31, '/uploads/files/1ffaaf76-11f7-4870-afa6-3f1b3fcb3cfa_BDD.pdf'),
(31, '/uploads/files/45885db6-dea5-4efa-91fe-a66def67c788_cas de tests.xlsx'),
(32, '/uploads/files/a6c82511-0022-4836-a8f1-cf4617dfa434_image 1.jpeg'),
(32, '/uploads/files/2bad1d00-b168-4b65-b153-6969c0d58d0a_BDD.pdf'),
(32, '/uploads/files/38ddc669-a23a-4743-a5a3-e499db450258_cas de tests.xlsx'),
(33, '/uploads/files/07077502-90a5-4ab6-9eab-18cc90fe1b5c_image 1.jpeg'),
(33, '/uploads/files/1f40d0c0-41f3-495e-a92e-be767979b9a1_BDD.pdf'),
(33, '/uploads/files/c7ee118b-9946-4c30-b6c3-d0b2dc5129f9_cas de tests.xlsx'),
(34, '/uploads/files/c9de7ab8-dbf4-4432-aa78-385e4b061c28_image 1.jpeg'),
(34, '/uploads/files/17266c3d-56d3-4c58-a587-a0b657bd11fa_BDD.pdf'),
(34, '/uploads/files/db6bd4c5-23ca-46b1-b27f-b6fe19eeec98_cas de tests.xlsx'),
(35, '/uploads/files/291a4649-0927-49f3-b0d6-88ee36bbbc30_image 1.jpeg'),
(35, '/uploads/files/643533e5-dd0f-4809-99a2-09a6cb9ec540_BDD.pdf'),
(35, '/uploads/files/58b6624b-aa9a-4b07-916b-565b2dd33b07_cas de tests.xlsx'),
(36, '/uploads/files/2970bf54-a2ad-415d-b3b4-109004c1b45e_image 1.jpeg'),
(36, '/uploads/files/cd32263d-eaca-478e-aaaa-dbc83383e72c_BDD.pdf'),
(36, '/uploads/files/bea9f090-36c4-4eb5-bf1a-a55b9acbdfda_cas de tests.xlsx'),
(37, '/uploads/files/7f24670b-fc1a-4707-9721-16281bdc98e5_image 1.jpeg'),
(37, '/uploads/files/b5eb7a6a-9c36-4c28-84da-0083d83358da_BDD.pdf'),
(37, '/uploads/files/be9f6945-c15c-4535-8f5c-0163665465ed_cas de tests.xlsx'),
(38, '/uploads/files/f8b9f0c1-f58f-47e8-913c-008fac4dc840_cas de tests.xlsx'),
(38, '/uploads/files/19dc94ff-98e9-4dfa-9b83-3a0cb8d4850b_image 1.jpeg'),
(38, '/uploads/files/ecd75c03-139a-4ffa-9cbc-031b2957a108_BDD.pdf'),
(39, '/uploads/files/0e5c3f57-b650-47ab-9df1-efef1d631394_image 1.jpeg'),
(39, '/uploads/files/01cea490-e6be-4b55-b6a0-da04596d6143_BDD.pdf'),
(39, '/uploads/files/a1dc5907-991b-4d52-86f3-f89b100e1ee8_cas de tests.xlsx'),
(40, '/uploads/files/325599da-2f0f-4fa8-8b68-9d5aa4c0312c_image 1.jpeg'),
(40, '/uploads/files/1a8f8379-4f66-4016-9415-6d6c76747ddf_BDD.pdf'),
(40, '/uploads/files/733a12b2-1897-4f1c-952b-c0f9c004f26b_cas de tests.xlsx'),
(41, '/uploads/files/c458ef74-6979-4560-a534-d4eccd54db76_image 1.jpeg'),
(41, '/uploads/files/09ecf3da-3ed2-4cec-a330-423f4179e0e7_BDD.pdf'),
(41, '/uploads/files/a2859588-d4b5-4649-af12-cf470a2f0540_cas de tests.xlsx'),
(42, '/uploads/files/d50b315a-9928-4715-96dd-0c3c96eba520_image 1.jpeg'),
(42, '/uploads/files/80dba045-c6e9-4e30-ba3f-8b2ba837b5e1_BDD.pdf'),
(42, '/uploads/files/8ce518d8-e071-4aaf-861f-f8d1886238c6_cas de tests.xlsx'),
(43, '/uploads/files/1892584d-f0cd-4f68-9dfc-f4896eb3b09d_image 1.jpeg'),
(43, '/uploads/files/ac352dff-4e34-4c0c-856a-931cca2ef98b_BDD.pdf'),
(43, '/uploads/files/3e0b45de-82b7-4e7d-8215-327e18ec5f35_cas de tests.xlsx'),
(44, '/uploads/files/728178be-5f7a-49da-9637-206fe17273fa_image 1.jpeg'),
(44, '/uploads/files/960e4c13-e870-46ce-97f1-e27592399a2f_BDD.pdf'),
(44, '/uploads/files/22739d4f-c927-46a6-90ad-a06803d4ef0c_cas de tests.xlsx'),
(45, '/uploads/files/63a40afd-376d-42e1-9200-fe8d1c80a434_image 1.jpeg'),
(45, '/uploads/files/2c240e1b-91c8-4885-a44f-64f5ffefdc5c_BDD.pdf'),
(45, '/uploads/files/e2b9bcc7-a732-4611-96a9-7b413fab0db9_cas de tests.xlsx'),
(46, '/uploads/files/68d381a7-96d3-427b-a245-fcd094d129c2_cas de tests.xlsx'),
(46, '/uploads/files/e2e8fd5b-4ce6-4468-9c1c-89bc1ec377d0_image 1.jpeg'),
(46, '/uploads/files/cb8e7216-d505-4690-aadb-9462f6c5bf81_BDD.pdf'),
(47, '/uploads/files/e3c741da-8bff-43c5-a699-9dd243c39c94_image 1.jpeg'),
(47, '/uploads/files/c33b664a-b3d1-4066-b2a2-af236cbb1918_BDD.pdf'),
(47, '/uploads/files/0ccc220a-6a81-4785-859d-2660d8f13bf1_cas de tests.xlsx'),
(48, '/uploads/files/7a0b8496-409d-4b38-b1c1-af97bf6f55a4_image 1.jpeg'),
(48, '/uploads/files/bf955480-313d-43ea-a465-c4c4bfee4662_BDD.pdf'),
(48, '/uploads/files/d650930d-acc1-49b7-8104-a7be0a220d94_cas de tests.xlsx'),
(49, '/uploads/files/6fb03766-cf48-4ee4-8e16-fd5c239fe216_image 1.jpeg'),
(49, '/uploads/files/63ff532d-f08c-4217-9845-06c2cb037897_BDD.pdf'),
(49, '/uploads/files/d4e654bb-4b8e-4cbf-9556-05a145aa2bdd_cas de tests.xlsx'),
(50, '/uploads/files/824440fd-6260-4357-a1a9-02bd959b423a_image 1.jpeg'),
(50, '/uploads/files/850a1b53-c18f-4be3-9e50-d7e4aff2fccf_BDD.pdf'),
(50, '/uploads/files/67f2bfb4-bd27-41c2-af0d-148ff246ba94_cas de tests.xlsx'),
(51, '/uploads/files/5d96535e-9417-4558-92a4-4f2a281af0ff_image 1.jpeg'),
(51, '/uploads/files/1c10bb00-db21-4b9a-9320-e2f695ccbf4d_BDD.pdf'),
(51, '/uploads/files/972de615-322d-4f80-bf81-08aefe07725d_cas de tests.xlsx'),
(52, '/uploads/files/8b399562-dfc5-41a8-a2da-3455affb3d66_image 1.jpeg'),
(52, '/uploads/files/ec31e89b-6b98-41f0-a135-536db8d9355b_BDD.pdf'),
(52, '/uploads/files/2ae7455f-5acc-40e5-aa6c-31b2de4d205d_cas de tests.xlsx'),
(53, '/uploads/files/1c8ef624-d0e2-4e65-a7e1-e2bcbbe1aedb_image 1.jpeg'),
(53, '/uploads/files/f5f0f2de-b513-4c1d-912f-d6852988ec4b_BDD.pdf'),
(53, '/uploads/files/f21faa6c-514a-484b-9a8a-c3b931d52344_cas de tests.xlsx'),
(54, '/uploads/files/3191a53e-8810-40f1-9557-eaba92819a67_image 1.jpeg'),
(54, '/uploads/files/bdc76001-2b0e-4c94-8a10-a53201f42d26_BDD.pdf'),
(54, '/uploads/files/8fc3c0ad-592c-42a9-870b-70d7757394d2_cas de tests.xlsx'),
(55, '/uploads/files/2a4dac60-c8b3-4db4-bd04-0c4a05ff1f6c_image 1.jpeg'),
(55, '/uploads/files/197efb1d-8648-4636-ae99-17e14f7d67ec_BDD.pdf'),
(55, '/uploads/files/52c2c2a7-667a-4570-85c8-a06ff05870d5_cas de tests.xlsx'),
(56, '/uploads/files/d9d8b8be-63c9-42a3-ba5b-672053e85a74_image 1.jpeg'),
(56, '/uploads/files/6b9e901c-23cf-423c-bd13-059db66628a0_BDD.pdf'),
(56, '/uploads/files/80d8acb5-8dee-460d-82e6-16555d683a59_cas de tests.xlsx'),
(57, '/uploads/files/bcf55f1a-aa2b-4d6d-ab51-796fc4a6c722_image 1.jpeg'),
(57, '/uploads/files/a51d47a8-8779-46aa-b25b-22460629da97_BDD.pdf'),
(57, '/uploads/files/d23b0bf2-38bf-43bf-8f7f-1a79e14f8fc9_cas de tests.xlsx'),
(58, '/uploads/files/31568d2c-dfe5-4632-b11b-bd9c638cc1d8_image 1.jpeg'),
(58, '/uploads/files/d76809f0-7c38-4547-9ee4-bd46222d7963_BDD.pdf'),
(58, '/uploads/files/de5bebaa-33ee-4ecb-a70d-f2a8c7674578_cas de tests.xlsx'),
(59, '/uploads/files/3026da8b-69b2-43a4-88ca-c46b82302df9_image 1.jpeg'),
(59, '/uploads/files/9e2339c9-0cb7-47a4-a80e-2868b9352c84_BDD.pdf'),
(59, '/uploads/files/54815d4e-ca8b-4a7e-98cc-3475ff5614c5_cas de tests.xlsx'),
(60, '/uploads/files/ae690c48-e044-433a-b55a-b3d2509a07bd_cas de tests.xlsx'),
(60, '/uploads/files/b920e27b-d4ee-4edc-b727-3f7a392a15a6_image 1.jpeg'),
(60, '/uploads/files/5dc80331-f143-42cb-947c-d2df1effc6f0_BDD.pdf'),
(61, '/uploads/files/6f6aaae2-a2a6-454b-b6db-7b9ae3724dad_image 1.jpeg'),
(61, '/uploads/files/189778fa-0c20-4627-998a-0a922a17511b_BDD.pdf'),
(61, '/uploads/files/8c797839-720b-4e58-abbe-f9b24b060136_cas de tests.xlsx'),
(62, '/uploads/files/755965aa-ce58-4f79-984a-7f6ebe6c3bf7_image 1.jpeg'),
(62, '/uploads/files/82aabef7-c10c-45bc-b964-d1c3c9bbc3ca_BDD.pdf'),
(62, '/uploads/files/0939352e-887d-4f66-bc64-87f10ea69a53_cas de tests.xlsx'),
(63, '/uploads/files/889fcc52-b894-4063-92eb-c7b7a87c53bf_image 1.jpeg'),
(63, '/uploads/files/1d04bc08-37dc-453c-a746-2ee3fec1d23a_BDD.pdf'),
(63, '/uploads/files/355aebc6-018f-4fef-b407-f7efef6f69fb_cas de tests.xlsx'),
(64, '/uploads/files/222dea1f-6aaa-444a-b828-b18c184610a3_image 1.jpeg'),
(64, '/uploads/files/335c3873-8bd9-4d85-b985-147acf4d0e3b_BDD.pdf'),
(64, '/uploads/files/2f237c68-b499-4c9f-abc2-36789b332640_cas de tests.xlsx'),
(65, '/uploads/files/7c32f921-a2ed-40a9-9faf-64c4248521fa_image 1.jpeg'),
(65, '/uploads/files/748d7ab9-4442-4c64-97c8-571d702e0a38_BDD.pdf'),
(65, '/uploads/files/737f8aed-78b8-42a6-847a-166016cde3ec_cas de tests.xlsx'),
(66, '/uploads/files/b00f2f7f-73f0-4f18-9bb4-e9f6bc24a237_image 1.jpeg'),
(66, '/uploads/files/cb9f5cef-356e-4f52-969c-e1d503d10dca_BDD.pdf'),
(66, '/uploads/files/08a4cd01-02aa-4e9b-a1c2-6c7f81a9c79b_cas de tests.xlsx'),
(67, '/uploads/files/dad94fb9-99b4-4593-b3b8-f54d00d258bb_image 1.jpeg'),
(67, '/uploads/files/b5fefb79-f927-4ac9-8607-890cca0219eb_BDD.pdf'),
(67, '/uploads/files/b699dd7a-c5bb-4c0c-a6c0-b64acefc46fd_cas de tests.xlsx'),
(68, '/uploads/files/87c1b1a6-acd5-40f1-b021-7394ebafbf3c_image 1.jpeg'),
(68, '/uploads/files/0a32c4d6-10c3-4a89-b33b-00531978056d_BDD.pdf'),
(68, '/uploads/files/0446392d-43ab-46a6-9495-394cc0113272_cas de tests.xlsx'),
(69, '/uploads/files/103d043f-5fb4-4363-af0f-dc969f2a4ce9_image 1.jpeg'),
(69, '/uploads/files/62efbec9-b8da-4b29-943f-def59fe609d6_BDD.pdf'),
(69, '/uploads/files/aa7a0eb3-9f73-41d2-815b-35186986f04e_cas de tests.xlsx'),
(70, '/uploads/files/4b88ffc6-38c0-4136-ae8d-88618e28a7c6_image 1.jpeg'),
(70, '/uploads/files/0f0b39c9-6a66-4d30-8c0f-4963ce48bc34_BDD.pdf'),
(70, '/uploads/files/5b1c41de-3bc6-4637-9494-ef098879346e_cas de tests.xlsx');

-- --------------------------------------------------------

--
-- Structure de la table `projet_membres`
--

CREATE TABLE `projet_membres` (
  `projet_id` bigint(20) NOT NULL,
  `user_matricule` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `projet_membres`
--

INSERT INTO `projet_membres` (`projet_id`, `user_matricule`) VALUES
(1, 'wafa'),
(2, 'salma'),
(2, 'chiraz'),
(2, 'fahed'),
(2, 'pouka'),
(2, 'sameh'),
(3, 'salma'),
(3, 'chiraz'),
(3, 'fahed'),
(3, 'pouka'),
(3, 'sameh'),
(4, 'salma'),
(4, 'chiraz'),
(4, 'fahed'),
(4, 'pouka'),
(4, 'sameh'),
(5, 'salma'),
(5, 'chiraz'),
(5, 'fahed'),
(5, 'pouka'),
(5, 'sameh'),
(6, 'salma'),
(6, 'chiraz'),
(6, 'fahed'),
(6, 'pouka'),
(6, 'sameh'),
(7, 'salma'),
(7, 'chiraz'),
(7, 'fahed'),
(7, 'pouka'),
(7, 'sameh'),
(9, 'salma'),
(9, 'chiraz'),
(9, 'fahed'),
(9, 'pouka'),
(9, 'sameh'),
(10, 'salma'),
(10, 'chiraz'),
(10, 'fahed'),
(10, 'pouka'),
(10, 'sameh'),
(11, 'salma'),
(11, 'chiraz'),
(11, 'fahed'),
(11, 'pouka'),
(11, 'sameh'),
(12, 'salma'),
(12, 'chiraz'),
(12, 'fahed'),
(12, 'pouka'),
(12, 'sameh'),
(13, 'salma'),
(13, 'chiraz'),
(13, 'fahed'),
(13, 'pouka'),
(13, 'sameh'),
(14, 'salma'),
(14, 'chiraz'),
(14, 'fahed'),
(14, 'pouka'),
(14, 'sameh'),
(15, 'salma'),
(15, 'chiraz'),
(15, 'fahed'),
(15, 'pouka'),
(15, 'sameh'),
(16, 'salma'),
(16, 'chiraz'),
(16, 'fahed'),
(16, 'pouka'),
(16, 'sameh'),
(17, 'salma'),
(17, 'chiraz'),
(17, 'fahed'),
(17, 'pouka'),
(17, 'sameh'),
(18, 'salma'),
(18, 'chiraz'),
(18, 'fahed'),
(18, 'pouka'),
(18, 'sameh'),
(19, 'salma'),
(19, 'chiraz'),
(19, 'fahed'),
(19, 'pouka'),
(19, 'sameh'),
(20, 'salma'),
(20, 'chiraz'),
(20, 'fahed'),
(20, 'pouka'),
(20, 'sameh'),
(21, 'salma'),
(21, 'chiraz'),
(21, 'fahed'),
(21, 'pouka'),
(21, 'sameh'),
(22, 'salma'),
(22, 'chiraz'),
(22, 'fahed'),
(23, 'salma'),
(23, 'chiraz'),
(23, 'fahed'),
(23, 'pouka'),
(23, 'sameh'),
(8, 'salma'),
(8, 'chiraz'),
(8, 'fahed'),
(8, 'pouka'),
(8, 'sameh'),
(24, 'salma'),
(24, 'chiraz'),
(24, 'fahed'),
(24, 'pouka'),
(24, 'sameh'),
(25, 'salma'),
(25, 'chiraz'),
(25, 'fahed'),
(25, 'pouka'),
(25, 'sameh'),
(26, 'salma'),
(26, 'chiraz'),
(26, 'fahed'),
(26, 'pouka'),
(26, 'sameh'),
(27, 'salma'),
(27, 'chiraz'),
(27, 'fahed'),
(28, 'salma'),
(28, 'chiraz'),
(28, 'fahed'),
(28, 'pouka'),
(28, 'sameh'),
(29, 'salma'),
(29, 'chiraz'),
(29, 'fahed'),
(29, 'pouka'),
(29, 'sameh'),
(30, 'salma'),
(30, 'chiraz'),
(30, 'fahed'),
(30, 'pouka'),
(30, 'sameh'),
(31, 'salma'),
(31, 'chiraz'),
(31, 'fahed'),
(31, 'pouka'),
(31, 'sameh'),
(32, 'salma'),
(32, 'chiraz'),
(32, 'fahed'),
(32, 'pouka'),
(32, 'sameh'),
(33, 'salma'),
(33, 'chiraz'),
(33, 'fahed'),
(33, 'pouka'),
(33, 'sameh'),
(34, 'salma'),
(34, 'chiraz'),
(34, 'fahed'),
(34, 'pouka'),
(34, 'sameh'),
(35, 'salma'),
(35, 'chiraz'),
(35, 'fahed'),
(35, 'pouka'),
(35, 'sameh'),
(36, 'salma'),
(36, 'chiraz'),
(36, 'fahed'),
(36, 'pouka'),
(36, 'sameh'),
(37, 'salma'),
(37, 'chiraz'),
(37, 'fahed'),
(37, 'pouka'),
(37, 'sameh'),
(38, 'salma'),
(38, 'chiraz'),
(38, 'fahed'),
(39, 'salma'),
(39, 'chiraz'),
(39, 'fahed'),
(39, 'pouka'),
(39, 'sameh'),
(40, 'salma'),
(40, 'chiraz'),
(40, 'fahed'),
(40, 'pouka'),
(40, 'sameh'),
(41, 'salma'),
(41, 'chiraz'),
(41, 'fahed'),
(41, 'pouka'),
(41, 'sameh'),
(42, 'salma'),
(42, 'chiraz'),
(42, 'fahed'),
(42, 'pouka'),
(42, 'sameh'),
(43, 'salma'),
(43, 'chiraz'),
(43, 'fahed'),
(43, 'pouka'),
(43, 'sameh'),
(44, 'salma'),
(44, 'chiraz'),
(44, 'fahed'),
(44, 'pouka'),
(44, 'sameh'),
(45, 'salma'),
(45, 'chiraz'),
(45, 'fahed'),
(45, 'pouka'),
(45, 'sameh'),
(46, 'salma'),
(46, 'chiraz'),
(46, 'fahed'),
(47, 'salma'),
(47, 'chiraz'),
(47, 'fahed'),
(47, 'pouka'),
(47, 'sameh'),
(48, 'salma'),
(48, 'chiraz'),
(48, 'fahed'),
(48, 'pouka'),
(48, 'sameh'),
(49, 'salma'),
(49, 'chiraz'),
(49, 'fahed'),
(49, 'pouka'),
(49, 'sameh'),
(50, 'salma'),
(50, 'chiraz'),
(50, 'fahed'),
(50, 'pouka'),
(50, 'sameh'),
(51, 'salma'),
(51, 'chiraz'),
(51, 'fahed'),
(51, 'pouka'),
(51, 'sameh'),
(52, 'salma'),
(52, 'chiraz'),
(52, 'fahed'),
(52, 'pouka'),
(52, 'sameh'),
(53, 'salma'),
(53, 'chiraz'),
(53, 'fahed'),
(53, 'pouka'),
(53, 'sameh'),
(54, 'salma'),
(54, 'chiraz'),
(54, 'fahed'),
(54, 'pouka'),
(54, 'sameh'),
(55, 'salma'),
(55, 'chiraz'),
(55, 'fahed'),
(55, 'pouka'),
(55, 'sameh'),
(56, 'salma'),
(56, 'chiraz'),
(56, 'fahed'),
(56, 'pouka'),
(56, 'sameh'),
(57, 'salma'),
(57, 'chiraz'),
(57, 'fahed'),
(57, 'pouka'),
(57, 'sameh'),
(58, 'salma'),
(58, 'chiraz'),
(58, 'fahed'),
(58, 'pouka'),
(58, 'sameh'),
(59, 'salma'),
(59, 'chiraz'),
(59, 'fahed'),
(59, 'pouka'),
(59, 'sameh'),
(60, 'salma'),
(60, 'chiraz'),
(60, 'fahed'),
(61, 'salma'),
(61, 'chiraz'),
(61, 'fahed'),
(61, 'pouka'),
(61, 'sameh'),
(62, 'salma'),
(62, 'chiraz'),
(62, 'fahed'),
(62, 'pouka'),
(62, 'sameh'),
(63, 'salma'),
(63, 'chiraz'),
(63, 'fahed'),
(63, 'pouka'),
(63, 'sameh'),
(64, 'salma'),
(64, 'chiraz'),
(64, 'fahed'),
(64, 'pouka'),
(64, 'sameh'),
(65, 'salma'),
(65, 'chiraz'),
(65, 'fahed'),
(65, 'pouka'),
(65, 'sameh'),
(66, 'salma'),
(66, 'chiraz'),
(66, 'fahed'),
(66, 'pouka'),
(66, 'sameh'),
(67, 'salma'),
(67, 'chiraz'),
(67, 'fahed'),
(67, 'pouka'),
(67, 'sameh'),
(68, 'salma'),
(68, 'chiraz'),
(68, 'fahed'),
(68, 'pouka'),
(68, 'sameh'),
(69, 'salma'),
(69, 'chiraz'),
(69, 'fahed'),
(69, 'pouka'),
(69, 'sameh'),
(70, 'salma'),
(70, 'chiraz'),
(70, 'fahed'),
(70, 'pouka'),
(70, 'sameh');

-- --------------------------------------------------------

--
-- Structure de la table `reactions`
--

CREATE TABLE `reactions` (
  `id` bigint(20) NOT NULL,
  `type` enum('LIKE','DONE','PERFECT','CLAP') NOT NULL,
  `message_id` bigint(20) NOT NULL,
  `user_id` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `taches`
--

CREATE TABLE `taches` (
  `id` bigint(20) NOT NULL,
  `archived` bit(1) NOT NULL,
  `date_creation` datetime(6) NOT NULL,
  `date_fin` datetime(6) DEFAULT NULL,
  `deadline_depassee_notifiee` bit(1) DEFAULT NULL,
  `deadline_proche_notifiee` bit(1) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `en_retard` bit(1) DEFAULT NULL,
  `etat` enum('A_FAIRE','EN_COURS','EN_REVISION','TERMINE','BLOQUE','ANNULE') DEFAULT NULL,
  `nombre_commentaires` int(11) DEFAULT NULL,
  `nombre_modifications` int(11) DEFAULT NULL,
  `priorite` enum('BASSE','MOYENNE','HAUTE','CRITIQUE') DEFAULT NULL,
  `temps_estime` int(11) DEFAULT NULL,
  `temps_passe` int(11) DEFAULT NULL,
  `titre` varchar(100) DEFAULT NULL,
  `assigneea_matricule` varchar(20) DEFAULT NULL,
  `projet_numero_projet` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `taches`
--

INSERT INTO `taches` (`id`, `archived`, `date_creation`, `date_fin`, `deadline_depassee_notifiee`, `deadline_proche_notifiee`, `description`, `en_retard`, `etat`, `nombre_commentaires`, `nombre_modifications`, `priorite`, `temps_estime`, `temps_passe`, `titre`, `assigneea_matricule`, `projet_numero_projet`) VALUES
(1, b'0', '2025-11-15 15:06:40.000000', NULL, b'0', b'0', '', b'0', 'A_FAIRE', 0, 0, 'BASSE', 0, 0, 'La Soumission comme passion#427beba0-9b7d-48bc-8338-8d51e9368567', NULL, 1),
(2, b'0', '2025-11-15 15:09:23.000000', NULL, b'0', b'0', '', b'0', 'A_FAIRE', 0, 0, 'BASSE', 0, 0, 'La Soumission comme passion#b2ea1ecf-6b84-40de-ae16-27a270c7d265', NULL, 1),
(3, b'0', '2025-11-15 15:27:59.000000', NULL, b'0', b'0', '', b'0', 'A_FAIRE', 0, 0, 'BASSE', 0, 0, 'LOdyssée invisible#d9e26837-5082-46c9-8c11-4b806b500d05', NULL, 1),
(4, b'0', '2025-11-15 15:29:56.000000', NULL, b'0', b'0', '', b'0', 'A_FAIRE', 0, 0, 'BASSE', 0, 0, 'La Peau des savants#81953a1a-18a9-482c-b7a4-ef423262246d', NULL, 1),
(5, b'0', '2025-11-15 15:31:42.000000', NULL, b'0', b'0', '', b'0', 'A_FAIRE', 0, 0, 'BASSE', 0, 0, 'La Discipline des orphelins#5fb5b00c-f854-4191-9b1b-e5ccf9c87c63', NULL, 1),
(6, b'0', '2025-11-15 17:10:55.000000', NULL, b'0', b'0', '', b'0', 'A_FAIRE', 0, 0, 'BASSE', 0, 0, 'LOdeur du sanglier#348e39f2-79a0-4085-9646-a1402e8776d8', NULL, 1),
(7, b'0', '2025-11-15 17:12:26.000000', NULL, b'0', b'0', '', b'0', 'EN_COURS', 0, 0, 'BASSE', 0, 0, 'La Peau des savants#ae3c4db3-20f5-4d0f-b0c2-80903b8799c8', NULL, 1),
(8, b'0', '2025-11-15 17:13:51.000000', NULL, b'0', b'0', '', b'0', 'EN_REVISION', 0, 0, 'BASSE', 0, 0, 'Le Couloir de tous les mépris#9d9d6dbc-3742-41a7-8c87-d340044be7b8', NULL, 1),
(9, b'0', '2025-11-15 17:15:32.000000', NULL, b'0', b'0', '', b'0', 'TERMINE', 0, 0, 'BASSE', 0, 0, 'La Peau des savants#a2b802b8-be99-4598-9905-20ce2d1da14d', NULL, 1),
(10, b'0', '2025-11-15 17:17:06.000000', NULL, b'0', b'0', '', b'0', 'BLOQUE', 0, 0, 'BASSE', 0, 0, 'LOdyssée invisible#360e0c2b-d068-447b-b01d-a077cc1f4f41', NULL, 1),
(11, b'0', '2025-11-15 17:18:30.000000', NULL, b'0', b'0', '', b'0', 'ANNULE', 0, 0, 'BASSE', 0, 0, 'La Promise du voyeur#47f127c8-1d5e-4185-af36-0c425d0899d0', NULL, 1),
(12, b'0', '2025-11-15 17:20:07.000000', NULL, b'0', b'0', '', b'0', 'A_FAIRE', 0, 0, 'BASSE', 0, 0, 'La Discipline des orphelins#ac7df66f-321f-4dce-84bb-35fcd9d2adaf', NULL, 1),
(13, b'0', '2025-11-15 17:21:24.000000', NULL, b'0', b'0', '', b'0', 'EN_COURS', 0, 0, 'BASSE', 0, 0, 'La Peau des savants#e332ce50-84bf-498c-9039-f9094fdb5331', NULL, 1),
(14, b'0', '2025-11-15 17:22:23.000000', NULL, b'0', b'0', '', b'0', 'EN_REVISION', 0, 0, 'BASSE', 0, 0, 'La Promise du voyeur#5b288f46-0243-4dee-902c-1c7bf95ebcdb', NULL, 1),
(15, b'0', '2025-11-15 17:23:23.000000', NULL, b'0', b'0', '', b'0', 'TERMINE', 0, 0, 'BASSE', 0, 0, 'La progéniture du mal#b56c18b1-a56b-49c3-bf57-bb805095ea88', NULL, 1),
(16, b'0', '2025-11-15 17:24:28.000000', NULL, b'0', b'0', '', b'0', 'BLOQUE', 0, 0, 'BASSE', 0, 0, 'Le Couloir de tous les mépris#efbe590a-e8a9-40ef-878a-d8974b7abcb3', NULL, 1),
(17, b'0', '2025-11-15 17:25:28.000000', NULL, b'0', b'0', '', b'0', 'ANNULE', 0, 0, 'BASSE', 0, 0, 'LOdyssée invisible#38d9f2a8-19a0-4b15-90aa-53f89847e1f4', NULL, 1),
(18, b'0', '2025-11-15 17:54:21.000000', NULL, b'0', b'0', '', b'0', 'A_FAIRE', 0, 0, 'BASSE', 0, 0, 'Pourquoi je mens ?#f8d171d4-ad19-4f3d-a4fe-cb1241a2f627', NULL, 1),
(19, b'0', '2025-11-15 17:55:24.000000', NULL, b'0', b'0', '', b'0', 'EN_COURS', 0, 0, 'BASSE', 0, 0, 'LOdeur du sanglier#ac808823-bd05-4472-938f-61f96c26f34b', NULL, 1),
(20, b'0', '2025-11-15 17:56:33.000000', NULL, b'0', b'0', '', b'0', 'EN_REVISION', 0, 0, 'BASSE', 0, 0, 'Pourquoi je mens ?#57cefeae-1cd2-4aba-a72e-575a7a869593', NULL, 1),
(21, b'0', '2025-11-15 17:57:41.000000', NULL, b'0', b'0', '', b'0', 'TERMINE', 0, 0, 'BASSE', 0, 0, 'La Discipline des orphelins#02e907ad-2149-4b59-96fd-597318a7084c', NULL, 1),
(22, b'0', '2025-11-15 17:58:48.000000', NULL, b'0', b'0', '', b'0', 'BLOQUE', 0, 0, 'BASSE', 0, 0, 'La Promise du voyeur#5edf42c3-4479-4b6d-8ff8-d1959d9627a8', NULL, 1),
(23, b'0', '2025-11-15 17:59:51.000000', NULL, b'0', b'0', '', b'0', 'ANNULE', 0, 0, 'BASSE', 0, 0, 'LOdyssée invisible#54c54b7c-a6bf-423a-b961-49878a4c89c2', NULL, 1),
(24, b'0', '2025-11-15 18:00:59.000000', NULL, b'0', b'0', '', b'0', 'A_FAIRE', 0, 0, 'BASSE', 0, 0, 'Pourquoi je mens ?#6c6f50dc-2fce-4b96-9d77-910b0ea0ce62', NULL, 1),
(25, b'0', '2025-11-15 18:02:06.000000', NULL, b'0', b'0', '', b'0', 'EN_COURS', 0, 0, 'BASSE', 0, 0, 'La Peau des savants#aac7dc32-5eee-4283-ac3e-cb2b665baad7', NULL, 1),
(26, b'0', '2025-11-15 18:03:15.000000', NULL, b'0', b'0', '', b'0', 'EN_REVISION', 0, 0, 'BASSE', 0, 0, 'Le Siècle de la rue voisine#8c5d7a76-9b52-4435-8d11-a73869a15253', NULL, 1),
(27, b'0', '2025-11-15 18:04:28.000000', NULL, b'0', b'0', '', b'0', 'TERMINE', 0, 0, 'BASSE', 0, 0, 'LOdeur du sanglier#03846606-a1ff-429c-a051-9302ee929f19', NULL, 1),
(28, b'0', '2025-11-15 18:05:36.000000', NULL, b'0', b'0', '', b'0', 'BLOQUE', 0, 0, 'BASSE', 0, 0, 'Le Couloir de tous les mépris#be939bff-8d73-4d1f-8cbf-9cfb048d51e7', NULL, 1),
(29, b'0', '2025-11-15 18:06:47.000000', NULL, b'0', b'0', '', b'0', 'ANNULE', 0, 0, 'BASSE', 0, 0, 'Le Désir des femmes fortes#44824b88-d5dc-42e5-af9e-85cd69866278', NULL, 1);

-- --------------------------------------------------------

--
-- Structure de la table `tache_membres`
--

CREATE TABLE `tache_membres` (
  `tache_id` bigint(20) NOT NULL,
  `user_matricule` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `tache_membres`
--

INSERT INTO `tache_membres` (`tache_id`, `user_matricule`) VALUES
(1, 'salma'),
(1, 'pouka'),
(1, 'sameh'),
(2, 'salma'),
(2, 'pouka'),
(2, 'sameh'),
(3, 'salma'),
(3, 'pouka'),
(3, 'sameh'),
(4, 'salma'),
(4, 'sameh'),
(5, 'salma'),
(5, 'sameh'),
(6, 'salma'),
(6, 'sameh'),
(7, 'salma'),
(7, 'sameh'),
(8, 'salma'),
(8, 'sameh'),
(9, 'salma'),
(9, 'sameh'),
(10, 'salma'),
(10, 'sameh'),
(11, 'salma'),
(11, 'sameh'),
(12, 'salma'),
(12, 'sameh'),
(13, 'salma'),
(13, 'sameh'),
(14, 'salma'),
(14, 'sameh'),
(15, 'salma'),
(15, 'sameh'),
(16, 'salma'),
(16, 'sameh'),
(17, 'salma'),
(17, 'sameh'),
(18, 'salma'),
(18, 'sameh'),
(19, 'salma'),
(19, 'sameh'),
(20, 'salma'),
(20, 'sameh'),
(21, 'salma'),
(21, 'sameh'),
(22, 'salma'),
(22, 'sameh'),
(23, 'salma'),
(23, 'sameh'),
(24, 'salma'),
(24, 'sameh'),
(25, 'salma'),
(25, 'sameh'),
(26, 'salma'),
(26, 'sameh'),
(27, 'salma'),
(27, 'sameh'),
(28, 'salma'),
(28, 'sameh'),
(29, 'salma'),
(29, 'sameh');

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

CREATE TABLE `users` (
  `matricule` varchar(20) NOT NULL,
  `date_creation` datetime(6) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `est_connecte` bit(1) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `is_deleted` bit(1) NOT NULL,
  `mot_de_passe` varchar(255) NOT NULL,
  `must_change_password` bit(1) DEFAULT NULL,
  `nom` varchar(50) DEFAULT NULL,
  `prenom` varchar(50) DEFAULT NULL,
  `reset_token` varchar(255) DEFAULT NULL,
  `reset_token_expiry` datetime(6) DEFAULT NULL,
  `role` enum('ADMIN','MANAGER','MEMBER') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `users`
--

INSERT INTO `users` (`matricule`, `date_creation`, `email`, `est_connecte`, `image`, `is_deleted`, `mot_de_passe`, `must_change_password`, `nom`, `prenom`, `reset_token`, `reset_token_expiry`, `role`) VALUES
('ala', '2025-11-14 20:25:56.000000', 'anaïs.rolland771@example.com', b'0', '3da71471-344b-4296-a381-4f3a5955ced8_image 3.jpeg', b'0', '$2a$10$SJ9X3kFgIZWB97pZ1th5c.mgo.aL6WdrlIOsTJzBGCQtUf9Ke46Ra', b'0', 'Marty', 'Anaïs', NULL, NULL, 'MANAGER'),
('arbia', NULL, 'clara.jacquet878@example.com', b'0', '623fbbbd-9115-4ee5-94f3-c49dcba93c86_image 3.jpeg', b'0', '$2a$10$gN62ta6JASWhT3a0t9W/reOAw9DEw/kiqcK1/G1u.tmkzA/BAQ9ny', b'0', 'Martinez', 'Clara', NULL, NULL, 'ADMIN'),
('auto_1763217180491', '2025-11-15 14:33:09.000000', 'auto_1763217180491@test.tld', b'0', '85742d32-257d-4736-a6e0-e9838f717c61_image 1.jpeg', b'0', '$2a$10$iT1nUM22BntkNw99R.Trpekn2fdgS2128z5vvwiM/xzmahY58tewu', b'1', 'AutoNom', 'AutoPrenom', NULL, NULL, 'MANAGER'),
('auto_1763217230915', '2025-11-15 14:34:12.000000', 'auto_1763217230915@test.tld', b'0', 'bca06bd5-67bf-4458-aebc-f3b71a8adde1_image 1.jpeg', b'1', '$2a$10$2n9mEHjCLheJFAuOI9akIOBfc8g9f2vEl74AeUJUQaSDhO3pfHHqW', b'1', 'AutoNom', 'AutoPrenom', NULL, NULL, 'MANAGER'),
('auto_1763227582060', '2025-11-15 17:26:30.000000', 'auto_1763227582060@test.tld', b'0', '9eb05575-76c2-4d97-8277-806cd7b6047f_image 1.jpeg', b'0', '$2a$10$IRc/2TTByIGMLKl3qX9ZyuJhIuvmDuOtFEyDk6cysNstGF/sNRKXS', b'1', 'AutoNom', 'AutoPrenom', NULL, NULL, 'MANAGER'),
('auto_1763227629195', '2025-11-15 17:27:19.000000', 'auto_1763227629195@test.tld', b'0', '79df4267-b04b-4a80-9fe3-ff3223232bfb_image 1.jpeg', b'1', '$2a$10$8JeK1ZIdXsUbTM0KsLU5qO6KMtHMh4QvWww06EMtpKfHS9VD9OcVK', b'1', 'AutoNom', 'AutoPrenom', NULL, NULL, 'MANAGER'),
('chiraz', '2025-11-14 20:42:07.000000', 'chiraz@gmail.com', b'0', '6bc2240a-7162-48dd-ba28-cea7b317b484_arboucha.png', b'0', '$2a$10$CJpbfyejW1xdNqWvM7dMgeYOkksJgFp4dPHuH/ea.IvXTvIdTA49m', b'1', 'mtiri', 'chiraz', NULL, NULL, 'MEMBER'),
('fahed', '2025-11-14 20:42:55.000000', 'fahed@gmail.com', b'0', 'd8a18e70-f887-4cc9-aa23-3881de9303ca_arboucha.png', b'0', '$2a$10$XfGlAk4xQr9tfAXHDkd6iukUDu5QPbFMHhHWOD867GWv02/Mp0Yie', b'1', 'mtiri', 'fahed', NULL, NULL, 'MEMBER'),
('pouka', '2025-11-14 20:44:05.000000', 'pouka@gmail.com', b'0', 'cc9725c6-510b-45c0-94e4-d21fd964bde1_arboucha.png', b'0', '$2a$10$V6L1DhAUFu6S4x7veP.80.6rouhJ1ScXT8hzbveFzNpIlHQz6pPhy', b'1', 'riahi ', 'pouka', NULL, NULL, 'MEMBER'),
('salma', '2025-11-14 20:40:13.000000', 'salma840@gmail.cm', b'0', 'dc8a02d4-f879-4abf-9ad6-7ab973df5a0a_arboucha.png', b'0', '$2a$10$AdAmzYGwgixfVOBrjCnp9Oz6rw.m1S1a4RE2Rff5qPNWJUsQBG/ry', b'1', 'riahi', 'salma', NULL, NULL, 'MEMBER'),
('sameh', '2025-11-14 21:02:09.000000', 'sameh@gmail.com', b'0', '517f3b1b-dbdc-45e6-8c56-6ab1bc3710ef_arboucha.png', b'0', '$2a$10$JKQ5zmNyjlVVjH2YFY5eC.EfnB/yRFfJ/YILEms09wqUXyYr3SDRO', b'1', 'riahi', 'sameh', NULL, NULL, 'MEMBER'),
('testrail', '2025-11-14 21:02:47.000000', 'test_reset@mailsac.com', b'0', '93b70e10-4693-4814-a48d-d837561754bf_arboucha.png', b'0', '$2a$10$RRjdskEJqXnzkfuMKDJa..VwGRFSFpsD1rbrFWIVB47Z3Yd9kKyl6', b'1', 'tesrail', 'testrail', '27427d31-5922-45f4-9b4a-23f258e78477', '2025-11-15 16:58:25.000000', 'MANAGER'),
('u04759021', '2025-11-15 17:38:57.000000', 'lola.roche909@example.com', b'0', 'dc36528e-cd23-41e6-8adb-a542f57f8178_image 1.jpeg', b'0', '$2a$10$brnafB5kO2iipcrX5YitY.rPTn6.8VirHNaapZTX7gjF3smLEJZbC', b'1', 'Roche', 'Lola', NULL, NULL, 'MANAGER'),
('u25857140', '2025-11-15 17:39:40.000000', 'clémence.louis680@example.com', b'0', '600740bb-621b-4e5b-9a3c-ff0841302428_image 1.jpeg', b'0', '$2a$10$i3dgc2qRmR.abSsjqFi/aeXxpdytqduXuRKxwL9tIln1gVLuqFLXq', b'1', 'Louis', 'Clémence', NULL, NULL, 'MANAGER'),
('u27133456', '2025-11-15 14:43:59.000000', 'antoine.renard602@example.com', b'0', '36c89339-07f4-49a3-b2bf-43a15d30e2bc_image 1.jpeg', b'0', '$2a$10$lIUWYpcVDQrcd6kNL5S3S..bymJiY.T.T2YaHvSzQccyweATZrOYm', b'1', 'Renard', 'Antoine', NULL, NULL, 'MANAGER'),
('u36156470', '2025-11-15 14:44:46.000000', 'julie.joly985@example.com', b'0', '28c18c73-062a-4cb6-8739-94413b3b4e7c_image 1.jpeg', b'0', '$2a$10$Zckp9/qohyPiRrhJoAzAeOeWLfZZ6pUJ1U/yG56DtghQbMzJ/QoN.', b'1', 'Joly', 'Julie', NULL, NULL, 'MANAGER'),
('u38736656', '2025-11-15 14:43:00.000000', 'pierre.barbier272@example.com', b'0', '12abb297-d1b0-42f2-857a-638ecedfef8d_image 1.jpeg', b'0', '$2a$10$/ZO6AxGKwNQcHaIPS2vRfOjaB0H9In6Ab0jIqRPmmDRiqLfM3KGwi', b'1', 'Barbier', 'Pierre', NULL, NULL, 'MANAGER'),
('u45741370', '2025-11-15 17:40:02.000000', 'mathis.legrand247@example.com', b'0', 'a6c48c75-ef7a-4d55-b670-5753513f2377_image 1.jpeg', b'0', '$2a$10$XlLF6GSiEFyuOmUbXcL.kedCGIuxxblzJHBM7wdex1PSRmQVOg/kK', b'1', 'Legrand', 'Mathis', NULL, NULL, 'MANAGER'),
('u50856289', '2025-11-15 14:41:42.000000', 'maxence.brunet840@example.com', b'0', 'daea1cf4-93fc-4a89-9407-3367a3c56a13_image 2.jpeg', b'0', '$2a$10$SR/BF8KYMb0Ns3VSID8IJ.9SJ4ZpgpKjxkApTxk2ZT5TpYv4I1rXK', b'1', 'Brunet', 'Maxence', NULL, NULL, 'MEMBER'),
('u59945625', '2025-11-15 14:35:10.000000', 'antoine.dupuis824@example.com', b'0', 'd62ffb32-ff31-425f-aa0c-eceaba8a7ff5_image 1.jpeg', b'0', '$2a$10$MdNcpoEyLjA8WPv17CcvM.9Av78f7UExpFYscdBZRXk0mkd9mXXMG', b'1', 'Dupuis', 'Antoine', NULL, NULL, 'MANAGER'),
('u66301909', '2025-11-15 17:40:31.000000', 'julien.leroy230@example.com', b'0', 'bf554360-a7ac-4f75-ab19-56ed25a80193_image 1.jpeg', b'0', '$2a$10$RbBRx9rq.RWz8Roo2.C0.ucUfz5G8dA7Wb/Db2Pqsu2WTBjdnd9.C', b'1', 'Leroy', 'Julien', NULL, NULL, 'MANAGER'),
('u67756036', '2025-11-15 17:36:47.000000', 'maxime.blanc688@example.com', b'0', '301d76a6-71a7-47b4-98e2-b01ca7b98669_image 2.jpeg', b'0', '$2a$10$veKkyo8DF9jpssyMZCMEhuKmFfa5oaAmUlui5UmD.7DPnb40PQ9w.', b'1', 'Blanc', 'Maxime', NULL, NULL, 'MEMBER'),
('u73593082', '2025-11-15 17:28:19.000000', 'chloé.paris447@example.com', b'0', '0e40454e-b8ca-4829-b38a-08a0023e7a1e_image 1.jpeg', b'0', '$2a$10$85GiHWj6M.DOPpmdM/JHhuSjLjQK3ibvBA7.IidD.fBkRbEx.Szpm', b'1', 'Paris', 'Chloé', NULL, NULL, 'MANAGER'),
('u73833166', '2025-11-15 14:45:41.000000', 'chloé.dubois249@example.com', b'0', '86310c10-21aa-460a-b865-01d702774e88_image 1.jpeg', b'0', '$2a$10$je9kJe1uVnuOCsmYV/FWkONJntLJVHnWuPtWnxeYWMQVHTbAjo.ju', b'1', 'Dubois', 'Chloé', NULL, NULL, 'MANAGER'),
('u83825308', '2025-11-15 14:42:20.000000', 'ethan.guillot282@example.com', b'0', '57243019-3c56-4f81-b8c7-13745d39be41_image 1.jpeg', b'0', '$2a$10$ftHVb278kVWa7Qau3dHeUORcElmuCWLYlf8LXWMwKWEWYvK3qDx3K', b'1', 'Collet', 'Mathilde', NULL, NULL, 'MANAGER'),
('u85782954', '2025-11-15 17:38:04.000000', 'pauline.julien477@example.com', b'0', '4a556796-f347-4a31-9c44-4c59e2ef1123_image 1.jpeg', b'0', '$2a$10$Qg.JBYH7TaKsq3LZeVdFx.uyyk9PFH8ZG5hUzbdc6qmcU4Q8hQHFO', b'1', 'Julien', 'Pauline', NULL, NULL, 'MANAGER'),
('u97854295', '2025-11-15 17:37:27.000000', 'enzo.gaillard486@example.com', b'0', 'ee8953eb-59b4-4648-8e05-ab5e59b378e1_image 1.jpeg', b'0', '$2a$10$zV7qN27IN6wlrIWXNeD6bOEfEJzo9r3AvNB.YlxpLkkiWGruCkhV6', b'1', 'Roux', 'Alice', NULL, NULL, 'MANAGER'),
('u98357008', '2025-11-15 14:45:13.000000', 'sami.new@gmail.com', b'0', 'fbce5e58-a7cc-4776-a64c-de47791d9d1a_image 1.jpeg', b'0', '$2a$10$sQf00pb5Hz8Mql.tGJVndOsR/ZH0sz1sj9spC6NDyzkXrqtYacq8O', b'1', 'David', 'Valentin', NULL, NULL, 'MANAGER'),
('usr1', '2025-11-15 14:36:57.000000', 'ala@gmail.com', b'0', 'b8606c05-ecf2-49b4-93ad-6def5d67a3a9_image 2.jpeg', b'0', '$2a$10$Phqh/QO.w8/ksuSftAS9wud45TqVCcwuR5vPkGrILC9RXUJHq/IQO', b'1', 'Ahmed', 'Ahmed', NULL, NULL, 'MEMBER'),
('wafa', '2025-11-14 20:28:29.000000', 'mathilde.brunet818@example.com', b'0', 'df5780e2-3753-4bdc-af86-203feb5b3ff9_image 3.jpeg', b'0', '$2a$10$ffd3wUCaxUYBQdqAJUJeq.GZDlRwbgs6eIIsHtHs3dTLEBxORKaa.', b'0', 'Brun', 'Mathilde', NULL, NULL, 'MEMBER');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `actualites`
--
ALTER TABLE `actualites`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `actualite_images`
--
ALTER TABLE `actualite_images`
  ADD KEY `FKfm43nkk3s9rxtb1sxxg8jly4i` (`actualite_id`);

--
-- Index pour la table `commentaires`
--
ALTER TABLE `commentaires`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK7y85lcrdtea3w0c77fkv86ua7` (`auteur_matricule`),
  ADD KEY `FK2gwv9d2pveq7r2fmlcqjviy2n` (`parent_comment_id`),
  ADD KEY `FKry9yhr222l89wbc9ugh1q14b7` (`tache_id`);

--
-- Index pour la table `commentaire_mentions`
--
ALTER TABLE `commentaire_mentions`
  ADD KEY `FKi5krtlswwwmrrlutbgayju77n` (`user_matricule`),
  ADD KEY `FK8lscp57u5gx8eo1ic6mvwu131` (`commentaire_id`);

--
-- Index pour la table `messages`
--
ALTER TABLE `messages`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK4ui4nnwntodh6wjvck53dbk9m` (`sender_id`);

--
-- Index pour la table `message_mentions`
--
ALTER TABLE `message_mentions`
  ADD PRIMARY KEY (`message_id`,`user_id`),
  ADD KEY `FKtl1wwfmsu5sj9bgf0j8eoadb4` (`user_id`);

--
-- Index pour la table `message_seen_by`
--
ALTER TABLE `message_seen_by`
  ADD PRIMARY KEY (`message_id`,`user_id`),
  ADD KEY `FKown1138i4leyy3fps321b8vl5` (`user_id`);

--
-- Index pour la table `notifications`
--
ALTER TABLE `notifications`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK75kaka4b7313kjcqypioltbmn` (`destinataire_matricule`),
  ADD KEY `FK8sf01q7qy34rf8p1j3npw6b14` (`projet_lie_numero_projet`),
  ADD KEY `FKmov8t2ak3kinst45dvbsim49p` (`tache_liee_id`);

--
-- Index pour la table `projets`
--
ALTER TABLE `projets`
  ADD PRIMARY KEY (`numero_projet`),
  ADD KEY `FKc0y5uxmf5r7owmvit6pbin2wm` (`chef_projet_matricule`);

--
-- Index pour la table `projet_fichiers`
--
ALTER TABLE `projet_fichiers`
  ADD KEY `FKrrmalaa78t1h9p8rkgjp1dafv` (`projet_id`);

--
-- Index pour la table `projet_membres`
--
ALTER TABLE `projet_membres`
  ADD KEY `FKg1vymebcb1hfb228c2wns6xl2` (`user_matricule`),
  ADD KEY `FKi1l3n1umlqoskxm45kk04c6wc` (`projet_id`);

--
-- Index pour la table `reactions`
--
ALTER TABLE `reactions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKlmwgdoo3g3f4g0wsgc1lmwfby` (`message_id`),
  ADD KEY `FKqmewaibcp5bxtlqxc2cawhuln` (`user_id`);

--
-- Index pour la table `taches`
--
ALTER TABLE `taches`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKh4iob37w7soihgjlgwicaxc07` (`assigneea_matricule`),
  ADD KEY `FKa8or78o5bhxae7w0slw5tkfeg` (`projet_numero_projet`);

--
-- Index pour la table `tache_membres`
--
ALTER TABLE `tache_membres`
  ADD KEY `FKlem8k5nk9kc28lkmdd9k96vib` (`user_matricule`),
  ADD KEY `FKks4k1pamlgg0r9h8vhd7l3fsa` (`tache_id`);

--
-- Index pour la table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`matricule`),
  ADD UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `actualites`
--
ALTER TABLE `actualites`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT pour la table `commentaires`
--
ALTER TABLE `commentaires`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `messages`
--
ALTER TABLE `messages`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `notifications`
--
ALTER TABLE `notifications`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=408;

--
-- AUTO_INCREMENT pour la table `projets`
--
ALTER TABLE `projets`
  MODIFY `numero_projet` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=71;

--
-- AUTO_INCREMENT pour la table `reactions`
--
ALTER TABLE `reactions`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `taches`
--
ALTER TABLE `taches`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `actualite_images`
--
ALTER TABLE `actualite_images`
  ADD CONSTRAINT `FKfm43nkk3s9rxtb1sxxg8jly4i` FOREIGN KEY (`actualite_id`) REFERENCES `actualites` (`id`);

--
-- Contraintes pour la table `commentaires`
--
ALTER TABLE `commentaires`
  ADD CONSTRAINT `FK2gwv9d2pveq7r2fmlcqjviy2n` FOREIGN KEY (`parent_comment_id`) REFERENCES `commentaires` (`id`),
  ADD CONSTRAINT `FK7y85lcrdtea3w0c77fkv86ua7` FOREIGN KEY (`auteur_matricule`) REFERENCES `users` (`matricule`),
  ADD CONSTRAINT `FKry9yhr222l89wbc9ugh1q14b7` FOREIGN KEY (`tache_id`) REFERENCES `taches` (`id`);

--
-- Contraintes pour la table `commentaire_mentions`
--
ALTER TABLE `commentaire_mentions`
  ADD CONSTRAINT `FK8lscp57u5gx8eo1ic6mvwu131` FOREIGN KEY (`commentaire_id`) REFERENCES `commentaires` (`id`),
  ADD CONSTRAINT `FKi5krtlswwwmrrlutbgayju77n` FOREIGN KEY (`user_matricule`) REFERENCES `users` (`matricule`);

--
-- Contraintes pour la table `messages`
--
ALTER TABLE `messages`
  ADD CONSTRAINT `FK4ui4nnwntodh6wjvck53dbk9m` FOREIGN KEY (`sender_id`) REFERENCES `users` (`matricule`) ON DELETE CASCADE;

--
-- Contraintes pour la table `message_mentions`
--
ALTER TABLE `message_mentions`
  ADD CONSTRAINT `FKn8dkm95maeahsjpbee9cnbntd` FOREIGN KEY (`message_id`) REFERENCES `messages` (`id`),
  ADD CONSTRAINT `FKtl1wwfmsu5sj9bgf0j8eoadb4` FOREIGN KEY (`user_id`) REFERENCES `users` (`matricule`);

--
-- Contraintes pour la table `message_seen_by`
--
ALTER TABLE `message_seen_by`
  ADD CONSTRAINT `FKgnf038c89ot511h0324n0jj80` FOREIGN KEY (`message_id`) REFERENCES `messages` (`id`),
  ADD CONSTRAINT `FKown1138i4leyy3fps321b8vl5` FOREIGN KEY (`user_id`) REFERENCES `users` (`matricule`);

--
-- Contraintes pour la table `notifications`
--
ALTER TABLE `notifications`
  ADD CONSTRAINT `FK75kaka4b7313kjcqypioltbmn` FOREIGN KEY (`destinataire_matricule`) REFERENCES `users` (`matricule`),
  ADD CONSTRAINT `FK8sf01q7qy34rf8p1j3npw6b14` FOREIGN KEY (`projet_lie_numero_projet`) REFERENCES `projets` (`numero_projet`),
  ADD CONSTRAINT `FKmov8t2ak3kinst45dvbsim49p` FOREIGN KEY (`tache_liee_id`) REFERENCES `taches` (`id`);

--
-- Contraintes pour la table `projets`
--
ALTER TABLE `projets`
  ADD CONSTRAINT `FKc0y5uxmf5r7owmvit6pbin2wm` FOREIGN KEY (`chef_projet_matricule`) REFERENCES `users` (`matricule`);

--
-- Contraintes pour la table `projet_fichiers`
--
ALTER TABLE `projet_fichiers`
  ADD CONSTRAINT `FKrrmalaa78t1h9p8rkgjp1dafv` FOREIGN KEY (`projet_id`) REFERENCES `projets` (`numero_projet`);

--
-- Contraintes pour la table `projet_membres`
--
ALTER TABLE `projet_membres`
  ADD CONSTRAINT `FKg1vymebcb1hfb228c2wns6xl2` FOREIGN KEY (`user_matricule`) REFERENCES `users` (`matricule`),
  ADD CONSTRAINT `FKi1l3n1umlqoskxm45kk04c6wc` FOREIGN KEY (`projet_id`) REFERENCES `projets` (`numero_projet`);

--
-- Contraintes pour la table `reactions`
--
ALTER TABLE `reactions`
  ADD CONSTRAINT `FKlmwgdoo3g3f4g0wsgc1lmwfby` FOREIGN KEY (`message_id`) REFERENCES `messages` (`id`),
  ADD CONSTRAINT `FKqmewaibcp5bxtlqxc2cawhuln` FOREIGN KEY (`user_id`) REFERENCES `users` (`matricule`);

--
-- Contraintes pour la table `taches`
--
ALTER TABLE `taches`
  ADD CONSTRAINT `FKa8or78o5bhxae7w0slw5tkfeg` FOREIGN KEY (`projet_numero_projet`) REFERENCES `projets` (`numero_projet`),
  ADD CONSTRAINT `FKh4iob37w7soihgjlgwicaxc07` FOREIGN KEY (`assigneea_matricule`) REFERENCES `users` (`matricule`);

--
-- Contraintes pour la table `tache_membres`
--
ALTER TABLE `tache_membres`
  ADD CONSTRAINT `FKks4k1pamlgg0r9h8vhd7l3fsa` FOREIGN KEY (`tache_id`) REFERENCES `taches` (`id`),
  ADD CONSTRAINT `FKlem8k5nk9kc28lkmdd9k96vib` FOREIGN KEY (`user_matricule`) REFERENCES `users` (`matricule`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
