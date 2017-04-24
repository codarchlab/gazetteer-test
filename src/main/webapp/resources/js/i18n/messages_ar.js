var messages = angular.module('gazetteer.messages', []);

messages.factory('messages', function(){
	return {
		"ui.error": "خطأ",
		"ui.contactAdmin": "في حال arachne@uni-koeln.de وجود المشكلة بشكل دائم يرجى الاتصال بـ",
		"ui.search.results": "نتيجة البحث",
		"ui.search.hits": "نتيجة",
		"ui.search.limit.10.tooltip": "أظهر ١٠ نتائج في الصفحة",
		"ui.search.limit.50.tooltip": "أظهر ٥٠ نتيجة في الصفحة",
		"ui.search.limit.100.tooltip": "أظهر ١٠٠ نتيجة في الصفحة",
		"ui.search.limit.1000.tooltip": "أظهر ١٠٠٠ نتيجة في الصفحة",
		"ui.search.sort.score.tooltip": "ترتيب حسب الصلة",
		"ui.search.sort.id.tooltip": "ترتيب حسب رقم التعريف",
		"ui.search.sort.name.tooltip": "ترتيب وفقاً للأسماء",
		"ui.search.sort.type.tooltip": "ترتيب وفقاً للنوع",
		"ui.search.sort.thesaurus.tooltip": "ترتيب وفقاً للمكنز",
		"ui.search.filter": "ترشيح",
		"ui.search.filter.coordinates": "مع إحداثيّات",
		"ui.search.filter.no-coordinates": "بدون إحداثيّات",
		"ui.search.filter.polygon": "مع مضلّع",
		"ui.search.filter.no-polygon": "بدون مضلّع",
		"ui.search.filter.unlocatable": "الموقع غير قابل للتحديد",
		"ui.search.filter.no-tags": "بلا وسوم",
		"ui.search.filter.no-provenance": "بدون ذكر المصدر",
		"ui.search.grandchildren-search": "جميع الأماكن الفرعية",
		"ui.place.names.more": "أكثر",
		"ui.place.names.less": "أقلّ",
		"ui.place.children.search": "أظهر الأماكن في البحث",
		"ui.place.save.success": "تمّ حفظ المكان بنجاح",
		"ui.place.save.failure": "لقد تعذّر حفظ المكان",
		"ui.place.save.failure.parentError": "لا يمكن أن يكون المكان تابعاً لذاته أو تابعاً لمكان يتبع له",
		"ui.place.save.failure.accessDeniedError": "ليس لديك الصلاحية اللازمة لتعديل هذا المكان",
		"ui.place.duplicate.success": "تمّ استنساخ المكان بنجاح",
		"ui.place.duplicate.failure": "لقد تعذّر استنساخ المكان",
		"ui.place.remove.success": "تمّ حذف المكان بنجاح",
		"ui.place.remove.failure": "لقد تعذّر حذف المكان",
		"ui.place.protected-site-login-info": "يرجى تسجيل الدخول لكي تتمكّن من الحصول على الإحداثيات الدقيقة.",
		"ui.place.protected-site-group-info": "ليس لديك السماحيات الضرورية الخاصّة بهذه المجموعة للتمكّن من الوصول إلى الإحداثيات الدقيقة. الرجاء الاتصال بمديري مجموعة البيانات.",
		"ui.place.protected-site-info": "الرجاء تسجيل الدخول من أجل الحصول على الإحداثيّات الدقيقة.",
		"ui.place.user-group-info": "عندما يتمّ تحديد مجموعة بيانات فإنّ هذا المكان يصبح مرئيّاً لأعضاء هذه المجموعة فقط. لا يمكن تغيير هذا الإعداد لاحقاً.",
		"ui.place.tags.info": "استخدم إذا أُمكن رجاءً الوسوم الموجودة مسبقاَ. لإنشاء وسومٍ جديدةٍ استخدم دوماً المصطلحات الانكليزية مكتوبة بأحرف صغيرة وبصيغة المفرد",
		"ui.thesaurus": "المكنز",
		"ui.create": "إنشاء مكان",
		"ui.link.tooltip": "اربط مع المكان الحالي",
		"ui.place.deleted": "تمّ حذف هذا المكان",
		"ui.place.hiddenPlace": "مكان محجوب",
		"ui.place.provenance-info": "يحتوي هذا المكان بيانات من المصادر المُدخلة.",
		"ui.place.replacing.first": "الموقع الذي له رقم معرّف",
		"ui.place.replacing.second": "تمّ الاستبدال. تمّ تحويلك إلى الإدخال الخاصّ بالموقع الجديد",
		"ui.merge.tooltip": "ادمج هذا المكان مع المكان الحالي",
		"ui.merge.success.head": "تمّ الدمج بنجاح",
		"ui.merge.success.body": "يرجى التحقّق من معلومات المكان المُنشأ حديثاً الواردة أدناه",
		"ui.extendedSearch": "بحث موسّع",
		"ui.change-history.change-type.create": "تمّ الإنشاء",
		"ui.change-history.change-type.edit": "تمّ التعديل",
		"ui.change-history.change-type.delete": "تمّ الحذف",
		"ui.change-history.change-type.merge": "تمّ الدمج",
		"ui.change-history.change-type.merge-with": "دُمِج مع",
		"ui.change-history.change-type.replace-with": "استُبدل بـ",
		"ui.change-history.change-type.replace": "تمّ الاستبدال",
		"ui.change-history.change-type.duplicate": "تمّ الاستنساخ",
		"ui.change-history.change-type.unknown": "مجهول",
		"ui.about": "عن",
		"ui.help": "مساعدة",
		"ui.recordGroupContact.success": "أُرسِلَت رسالتك بنجاح",
		"ui.recordGroupContact.error": "حصل خطأ عند إرسال رسالتك",
		"ui.shape-editor.error.wkt.invalidgeometrytype": "Invalid geometry type. The geometry type must be either 'POLYGON' or 'MULTIPOLYGON'.",
		"ui.shape-editor.error.wkt.missingbracket": "Closing bracket is missing.",
		"ui.shape-editor.error.wkt.notanumber.1": "Coordinate ",
		"ui.shape-editor.error.wkt.notanumber.2": " is not a number.",
		"ui.shape-editor.error.wkt.genericparsingerror": "Failed to parse WKT data.",
		"ui.shape-editor.error.geojson.invalidjson": "Failed to parse JSON data. The input data is not a valid JSON string.",
		"ui.shape-editor.error.geojson.invalidtype": "Invalid type. The type of the GeoJSON object must be 'Feature'.",
		"ui.shape-editor.error.geojson.nogeometry": "No geometry object of type 'Polygon' or 'MultiPolygon' could be found.",
		"ui.shape-editor.error.geojson.invalidgeometrytype.1": "'",
		"ui.shape-editor.error.geojson.invalidgeometrytype.2": "' is no supported geometry type. The geometry type must be either 'Polygon' or 'MultiPolygon'.",
		"ui.shape-editor.error.geojson.emptycoordinates": "The geometry object does not contain any coordinates.",
		"ui.shape-editor.error.validation.pathnotclosed.1": "The path starting with the point (",
		"ui.shape-editor.error.validation.pathnotclosed.2": ") is not closed. The first point in each path must equal the last one.",
		"ui.shape-editor.error.validation.duplicatepoint.1": "Duplicate point (",
		"ui.shape-editor.error.validation.duplicatepoint.2": "). Please ensure there are no duplicates except the first and last point of each path.",
		"ui.shape-editor.error.validation.lineintersection.1": "Line intersection at ",
		"ui.shape-editor.error.validation.lineintersection.2": ". Please ensure the geometry does not contain any intersecting lines.",
		"ui.shape-editor.error.validation.incompletecoordinates": "Incomplete coordinates.",
		"ui.shape-editor.error.validation.genericvalidationerror": "Validation failed.",
		"place.name.ancient": "قديم",
		"place.name.transliterated": "منقول حرفياً",
		"place.types.no-type": "- لا يوجد نو -",
		"place.types.archaeological-site": "موقع أثري",
		"place.types.archaeological-area": "منطقة أثرية",
		"place.types.continent": "قارّة",
		"place.types.administrative-unit": "وحدة إدارية",
		"place.types.populated-place": "مكان مأهول",
		"place.types.building-institution": "المكان/الهيئة",
		"place.types.landform": "التضاريس",
		"place.types.island": "جزيرة",
		"place.types.hydrography": "المسطّحات المائيّة",
		"place.types.landcover": "الغطاء النباتي",
		"place.types.description.archaeological-site": "(مواقع ذات بُنى أثرية (روما، الأكروبول، قطّاع تنقيبي",
		"place.types.description.archaeological-area": "منطقة حضارية محدّدة أثرياً وتقسيمات إدارية تاريخية: ليديا، إفريقيا البروقنصلية",
		"place.types.description.continent": "كتلٌ قاريةٌ مفصولة بواسطة حدود طبيعية أو تاريخية: إفريقيا",
		"place.types.description.administrative-unit": "وحدات إدارية محدّدة سياسياً: ألمانيا، إقليم أتيكا، عمّان الكبرى",
		"place.types.description.populated-place": "(موقع مأهول بالسكّان (روما، قرقنة",
		"place.types.description.building-institution": "مواقع متاحف أو هيئات أخرى: متحف البيرغامون، معهد الآثار الألماني في القاهرة",
		"place.types.description.landform": "(مَعلَم تضريسي جيومورفولوجي (جبل، وادي، مغارة",
		"place.types.description.island": "(يابسةٌ محاطةٌ تماماًبالمياه (كريت",
		"place.types.description.hydrography": ":(جميع المسطحات المائية الكبيرة (الراكدة أو الجارية المحيط الأطلسي، بحيرة طبرية، النيل",
		"place.types.description.landcover": "غطاء القشرة الأرضية الفيزيائي والبيولوجي: غابة، صحراء",
		"place.types.groups.physical-geographic": "وحدات جغرافية طبيعية",
		"place.types.groups.human-geographic": "وحدات جغرافية بشرية",
		"place.types.groups.archaeological": "وحدات أثرية وحضارية تاريخية",
		"place.types.groups.building": "مبنى",
		"place.unlocatable": "مكان لا يمكن تحديد موقعه",
		"location.confidence.0": "لا توجد معطيات",
		"location.confidence.1": "غير دقيق",
		"location.confidence.2": "دقيق",
		"location.confidence.3": "دقيق للغاية",
		"location.confidence.4": "خاطئ",
		"location.public": "إحداثيات مفتوحة للعموم",
		"domain.place.parent": "يقع ضمن",
		"domain.place.types": "النوع",
		"domain.place.tags": "الوسوم"
	};
});