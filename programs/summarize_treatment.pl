#!/ActivePerl/perl/bin/perl

####################################################################################################################
# File summarize_treatment.pl (Main program)
# Author: Marcelo Fiszman
# Purpose: Transforms a list of SemRep predications into a conceptual condensate
#
# Precondition:  It requires a list of predications as input and
# a literal  correct Metathesaurus concept as it appears from the preferred name of Metamap.
#
# Postcondition: Produces a conceptual condensate for the given Metathesaurus concept according
# to transformation stages principles that are based on the abstraction paradigm of summarization.
#
# Observation: The transformation stages in this program uses first a small preliminary filter and four principles:
# 1 -  A preliminary filter
# 2 -  Relevance - A Disorder schema from Burgun
# 3 -  Connectivity -  on shared arguments (not recursing at the present time more than once)
# 4 -  Novelty- Hierarchical pruning of uninformative predications such as Drugs|TREATS|Disease.
# 5 -  Saliency - Udo Hahns rules about salient concepts.
####################################################################################################################

use Config;
use Globals;



# Hash for domains
my %disorders = (
	"acab", 0, "anab", 0, "cgab", 0, "comd", 0, "dsyn", 0,
	"inpo", 0, "mobd", 0, "neop", 0, "patf", 0, "sosy", 0
);

my %cancause = (
	"aapp", 0, "antb", 0, "bact", 0, "bacs", 0, "elii", 0,
	"fngs", 0, "gngm", 0, "hops", 0, "imft", 0, "invt", 0,
	"opco", 0, "rich", 0, "virs", 0
);

my %orgcause = (
	"bact", 0, "fngs", 0, "invt", 0, "rich", 0, "virs", 0
);

my %canchem = (
	"aapp", 0, "antb", 0, "bacs", 0, "carb", 0, "eico", 0, "elii", 0,
	"gngm", 0, "hops", 0, "horm", 0, "imft", 0, "lipd", 0, "opco", 0,
	"orch", 0, "phsu", 0, "strd", 0, "topp", 0, "vita", 0
);

%cantreat = (
	"aapp", 0, "antb", 0, "carb", 0, "eico", 0, "gngm", 0, "horm", 0,
	"lipd", 0, "orch", 0, "phsu", 0, "strd", 0, "topp", 0, "vita", 0
);

# %human = ("aggp", 0, "famg", 0, "grup", 0,
#	  "humn", 0, "podg", 0, "prog", 0
# );
# popg was added, Dongwook, May 4 2009
%human = ("aggp", 0, "famg", 0, "grup", 0,
	  "humn", 0, "podg", 0, "popg", 0, "prog", 0
);

%canbeloc = ( "anst", 0, "blor", 0, "bpoc", 0, "bsoj", 0, "ffas", 0 );

%cancauselivb = ( "bact", 0, "fngs", 0, "invt", 0, "rich", 0, "virs", 0 );

%agegroup = ( "humn", 0 );

%dominant_preds = ("PROCESS_OF", 0);
# Vocabularies
@vocabularies = ( "CSP", "RCD", "MSH" );

%seen_skr_calls = ();
$osname         = $Config{'osname'};
#print "\nPlease type in the file name -----> ";
$input_file = $ARGV[0];
chomp($input_file);
#print "\nPlese select a disease (must type correct Meta concept) ------> ";
$seed = $ARGV[1];
$seed =~s/_/ /g;

print "\n $seed ";
chomp($seed);

#print "\nPlese select depth of search -----> ";
#$depth = <STDIN>;
#chomp($depth);

# debug or not
$debug_in = "debug";
$debug = 1;
chomp($debug_in);

# Start debug
if ( $debug_in eq "debug" ) {
	open( FD, ">$input_file.debug" ) || die "Cannot open $input_file.debug\n";
	$debug = 1;
}

#####################################################################
# main program
@novel_concepts = &Globals::GetNovelConcepts("treat_novelty_concept_list.txt");
%sent_hash      = &Globals::GetSentences($input_file);
%rel_hash       = &Globals::GetRelations($input_file);


# pre-filter
%prefiltered_rels = &Globals::PreFilter( \%rel_hash );

# Relevance
%relevant_rels    = &Relevance( \%prefiltered_rels );
$relevant_outfile = $input_file;
$relevant_outfile .= ".relevant";
&Globals::PrintHash( \%relevant_rels, $relevant_outfile, %sent_hash );
print "Relevance complete...\n";

# Connectivity
%connected_rels    = &Connectivity( \%relevant_rels );
$connected_outfile = $input_file;
$connected_outfile .= ".connected";
&Globals::PrintHash( \%connected_rels, $connected_outfile, %sent_hash );
print "Connectivity complete...\n";

# Novelty
print "Processing predications for novelty...\n";
%novel_rels    = &Novelty( \%connected_rels );
$novel_outfile = $input_file;
$novel_outfile .= ".novel";
&Globals::PrintHash( \%novel_rels, $novel_outfile, %sent_hash );
print "Novelty complete...\n";

# Saliency
print "Processing predications for saliency...\n";
@saliency_arr   = &Saliency( \%novel_rels );
%rels_after_SCs = %{ $saliency_arr[0] };
%rels_after_SR  = %{ $saliency_arr[1] };
%rels_after_SRC = %{ $saliency_arr[2] };
$SC_outfile     = $input_file;
$SC_outfile .= ".salient_concepts";
&Globals::PrintHash( \%rels_after_SCs, $SC_outfile, %sent_hash );
$SR_outfile = $input_file;
$SR_outfile .= ".salient_relations";
&Globals::PrintHash( \%rels_after_SR, $SR_outfile, %sent_hash );
$SRC_outfile = $input_file;
$SRC_outfile .= ".salient_predications";
&Globals::PrintHash( \%rels_after_SRC, $SRC_outfile, %sent_hash );
print "Saliency complete...\n";

# End debug
if ( $debug == 1 ) {
	close(FD);
}
##########################################################################


################################################################################
# Relevance: Finds the relevant predications by checking the predications
# against the schema constraints.
# Takes in a hash of filtered relations, and returns a hash of relevant relations.
#################################################################################
sub Relevance {
	my ($in_rel) = @_;
	my (%rels)   = %$in_rel;
	undef %relevance_hash;
	foreach $pmid ( keys %rels ) {
		@rel_arr = @{ $rels{$pmid} };
		undef @relevance_arr;
		foreach $rel (@rel_arr) {
			@rel_elements = split( /\|/, $rel );

			$subj = &Globals::ExtractConcepts( $rel_elements[3],  $rel_elements[7] );
			$obj  = &Globals::ExtractConcepts( $rel_elements[10], $rel_elements[14] );
			if ( $subj eq $seed || $obj eq $seed ) {
				$predicate    = $rel_elements[8];
				$subj_semtype = $rel_elements[5];
				$obj_semtype  = $rel_elements[12];
				unless ( $predicate =~ /ISA\(SPEC\)/ ) {
					$predicate =~ s/\(SPEC\)//g;
					$predicate =~ s/\(INFER\)//g;
					$rel       =~ s/\(SPEC\)//g;
					$rel       =~ s/\(INFER\)//g;

					#Schema constrain rules for CAUSES predicate
					if ( $predicate =~ /CAUSES$/ ) {
						if (   exists( $cancause{$subj_semtype} )
							&& exists( $disorders{$obj_semtype} ) )
						{
							push @relevance_arr, $rel;
						}
					}

					#Schema constrain rules for TREATS and PREVENTS predicates
					elsif ($predicate =~ /TREATS$/
						|| $predicate =~ /PREVENTS$/ )
					{
						if (   exists( $cantreat{$subj_semtype} )
							&& exists( $disorders{$obj_semtype} ) )
						{
							push @relevance_arr, $rel;
						}
					}

					#Schema Constrain rules for LOCATION  predicate
					elsif ( $predicate =~ /LOCATION_OF$/ ) {
						if (   exists( $canbeloc{$subj_semtype} )
							&& exists( $disorders{$obj_semtype} ) )
						{
							push @relevance_arr, $rel;
						}
					}

					#Schema Constrain rules for COEXISTS_WITH and ISA predicates
					elsif ($predicate =~ /COEXISTS_WITH$/)
					{
						if (   exists( $disorders{$subj_semtype} )
							&& exists( $disorders{$obj_semtype} ) )
						{
							push @relevance_arr, $rel;
						}
					}

					#Schema Constrain rules for PROCESS_OF predicate
					elsif ( $predicate =~ /PROCESS_OF$/ ) {
						if (
							exists( $disorders{$subj_semtype} )
							&& (   exists( $disorders{$obj_semtype} )
								|| exists( $agegroup{$obj_semtype} ) )
						  )
						{
							push @relevance_arr, $rel;
						}
					}

                                        #Schema Constrain rules for  ISA predicates
					elsif ( $predicate =~ /ISA$/ )
					{
						if (   (exists( $disorders{$subj_semtype} )
							&& exists( $disorders{$obj_semtype} )) ||
                                                       (exists( $cancause{$subj_semtype} )
							&& exists( $cancause{$obj_semtype} )) ||
                                                       (exists( $canchem{$subj_semtype} )
							&& exists( $canchem{$obj_semtype} ) ) )
						{
							push @relevance_arr, $rel;
						}
					}
				}
			}
		}
		if ( $#relevance_arr > -1 ) {
			$relevance_hash{$pmid} = [@relevance_arr];
		}
	}
	return %relevance_hash;
}

################################################################################
# Connectivity: Adds predications to the condensate by finding those that are
# connected to the seed concept through one and only one concept.
# Takes in a hash of relevant relations, and returns a hash of connected relations.
#################################################################################
sub Connectivity {
	my ($in_rel) = @_;
	my (%rels)   = %$in_rel;
	undef %connectivity_hash;

	# if either of the arguments is the seed concept, add the
	# other argument to a hash.
	foreach $pmid ( keys %rels ) {
		@rel_arr = @{ $rels{$pmid} };
		undef @newset;
		foreach $rel (@rel_arr) {
			unless ( $rel =~ /LOCATION_OF\|/
				|| $rel =~ /PROCESS_OF\|/
				|| $rel =~ /COEXISTS_WITH\|/ )
                        # unless ( $rel eq 'LOCATION_OF'
			#	|| $rel eq 'PROCESS_OF'
			#	|| $rel eq 'COEXISTS_WITH' )
                        {
				@rel_elements = split( /\|/, $rel );
				$subj = &Globals::ExtractConcepts( $rel_elements[3], $rel_elements[7] );
				$obj = &Globals::ExtractConcepts( $rel_elements[10], $rel_elements[14] );
				if ( $subj eq $seed ) {
					$rel_args_str = $obj;
				}
				elsif ( $obj eq $seed ) {
					$rel_args_str = $subj;
				}
				unless ( $rel_args_str eq "" ) {
					$newset{$rel_args_str} = 1;
				}
			}
		}
	}

	if ( $debug == 1 ) {
		for $n ( keys %newset ) {
		       print FD "In newset: $n\n";
                       #print "In newset: $n\n";
		}
	}

	# from the non-relevant predications, add those
	# with a concept that is in the newset hash.
	foreach $pmid ( keys %prefiltered_rels ) {
		@pred_arr     = @{ $prefiltered_rels{$pmid} };
		@relevant_arr = @{ $rels{$pmid} };
		undef @connectivity_arr;
		push @connectivity_arr, @relevant_arr;
		foreach $pred (@pred_arr) {
			$pred =~ s/\(SPEC\)//g;
			$pred =~ s/\(INFER\)//g;
			unless ( grep( /\Q$pred/, @relevant_arr ) ) {
				@pred_elements = split( /\|/, $pred );
				$pred_args_str1 =
				  &Globals::ExtractConcepts( $pred_elements[3], $pred_elements[7] );
				$pred_args_str2 =
				  &Globals::ExtractConcepts( $pred_elements[10], $pred_elements[14] );
				if (   exists $newset{$pred_args_str1}
					|| exists $newset{$pred_args_str2} )
				{
					push @connectivity_arr, $pred;
				}
			}
		}
		if ( $#connectivity_arr > -1 ) {
			$connectivity_hash{$pmid} = [@connectivity_arr];
		}
	}

	return %connectivity_hash;
}

###################################################################################
# Saliency: Removes predications from the condensate by by applying Udo Hahn's
# saliency operators.
# Takes in a hash of novel relations, and returns an array of hashes with size 3.
# The first hash contains relations that satisfy SC operators, the second the SR
# operators and the third the SRC operators.
###################################################################################
sub Saliency {
	my ($ref_novel_rels) = @_;
	%novel_rels = %$ref_novel_rels;

	# creates a hash table with concept and counts
	%concept_count_hash = &CountConcepts( \%novel_rels );

	# calculates average activation weights for the concepts
	$avgactwht = &ComputeAvgActivationWeight( \%concept_count_hash );
	if ( $debug == 1 ) {
		print FD "The average activation weight for SC1 is $avgactwht\n";
                #print "The average activation weight for SC1 is $avgactwht\n";
	}

	# saliency operators for concepts
	%sc1 = &ComputeSC1( $avgactwht, \%concept_count_hash );
	%concepts_avgsumoth = &ComputeAvgSumOther( \%concept_count_hash );
	%sc2 = &ComputeSC2( \%concepts_avgsumoth, \%concept_count_hash );
	%sc3 = &ComputeSC3( \%novel_rels );

	#incorporate concept saliency rules to predications.
	%rels_after_SCs = &IncorpSC( \%novel_rels, \%sc1, \%sc2, \%sc3 );

	# saliency operators for relationships
	%sr1 = &ComputeSR1( \%novel_rels, \%sc1 );

	# incorporate relation saliency rules to predications.
	%rels_after_SR = &IncorpSR( \%rels_after_SCs, \%sr1 );

	# saliency operators for predications
	# creates a hash table with concept and counts
	%predication_hash = &CountPredications( \%novel_rels );
	$balance_coeff    = &ComputeBalanceCoefficient( \%predication_hash );
	%balanced_predication_hash =
	  &BalancePredications( \%predication_hash, $balance_coeff );
	$avgactwht_preds =
	  &ComputeAvgActivationWeight( \%balanced_predication_hash );
	%src1             = &ComputeSRC1( $avgactwht_preds, \%balanced_predication_hash );

	#incorporate predications  my saliency rules to predications
	%rels_after_SRC = &IncorpSRC( \%rels_after_SCs, \%src1 );

	push @out_arr, {%rels_after_SCs};
	push @out_arr, {%rels_after_SR};
	push @out_arr, {%rels_after_SRC};
	return @out_arr;
}

#######################################################################################
# ComputeSC1: Creates a hash of saliency based on the concept frequency. (concept
# saliency operator 1)
# Takes in a hash of concept frequencies and an average activation weight
# Returns a hash of concept saliencies. Key is concept, value is {0,1} for salient and
# non-salient respectively.
#######################################################################################
sub ComputeSC1 {
	my ( $loc_avgactwht, $loc_concepts_in ) = @_;
	%loc_concepts = %$loc_concepts_in;
	local ( $conc, $loc_sc1 );
	foreach $conc ( keys %loc_concepts ) {
		if ( $loc_concepts{$conc} > $loc_avgactwht ) {
			$loc_sc1{$conc} = 1;
		}
		else {
			$loc_sc1{$conc} = 0;
		}
	}

	if ( $debug == 1 ) {
		foreach ( keys %loc_sc1 ) {
			if ( $loc_sc1{$_} == 1 ) {
				print FD "SC1:$_\n";
                                #print "SC1:$_\n";
			}
		}
	}

	return %loc_sc1;
}

#######################################################################################
# ComputeSC2: Creates a hash of saliency based on the average sum of other active
# concepts. (concept saliency operator 2)
# Takes in a hash of concept frequencies and an average sum of other concepts.
# Returns a hash of concept saliencies. Key is concept, value is {0,1} for salient and
# non-salient respectively.
#######################################################################################
sub ComputeSC2 {
	my ( $ref_conc_avgsumoth, $ref_concepts ) = @_;
	local ( $conc, $loc_avgsumoth, %loc_sc2, %loc_concepts,
		%loc_conc_avgsumoth );
	%loc_conc_avgsumoth = %$ref_conc_avgsumoth;
	%loc_concepts       = %$ref_concepts;
	foreach $conc ( keys %loc_concepts ) {
		if ( exists( $loc_conc_avgsumoth{$conc} ) ) {
			$loc_avgsumoth = $loc_conc_avgsumoth{$conc};
			if ( $loc_concepts{$conc} > $loc_avgsumoth ) {
				$loc_sc2{$conc} = 1;
			}
			else {
				$loc_sc2{$conc} = 0;
			}
		}
	}
	if ( $debug == 1 ) {
		foreach ( keys %loc_sc2 ) {
			if ( $loc_sc2{$_} == 1 ) {
				print FD "SC2:$_\n";
                                #print "SC2:$_\n";
			}
		}
	}
	return %loc_sc2;
}

#######################################################################################
# ComputeSC3: Creates a hash of saliency based on the average sum of other concepts.
# Predications are unique'd first. (concept saliency operator 3)
# Takes in a hash of relations.
# Returns a hash of concept saliencies. Key is concept, value is {0,1} for salient and
# non-salient respectively.
#######################################################################################
sub ComputeSC3 {
	my ($rels_in) = @_;
	my (%rels)    = %$rels_in;

	#unique pedications in list
	%seen = ();
	local ( @rel_elements, $item, %seen, @rels_uniq, %concepts_uniq,
		%concepts_uniq_avgsumoth, %loc_sc3 );
	foreach $pmid ( keys %rels ) {
		@rel_arr = @{ $rels{$pmid} };
		foreach $rel (@rel_arr) {
			@rel_elements = split( /\|/, $rel );
			$rel =
			    &Globals::ExtractConcepts( $rel_elements[3], $rel_elements[7] ) . "|"
			  . $rel_elements[8] . "|"
			  . &Globals::ExtractConcepts( $rel_elements[10], $rel_elements[14] );
			unless ( $seen{$rel} ) {
				push @rels_uniq, $rel;
				$seen{$rel} = 1;
			}
		}
	}

	if ( $debug == 1 ) {
		print FD "Uniq relation count: " . ( scalar @rels_uniq ) . "\n";
               # print "Uniq relation count: " . ( scalar @rels_uniq ) . "\n";
		foreach (@rels_uniq) {
			print FD "$_\n";
                        # print "$_\n";
		}
	}

	#now use same methodology as sc2
	%concepts_uniq           = &CountConceptArray( \@rels_uniq );
	%concepts_uniq_avgsumoth = &ComputeAvgSumOther( \%concepts_uniq );
	%loc_sc3 = &ComputeSC2( \%concepts_uniq_avgsumoth, \%concepts_uniq );

	if ( $debug == 1 ) {
		foreach ( keys %loc_sc3 ) {
			if ( $loc_sc3{$_} == 1 ) {
				print FD "SC3:$_\n";
                                #print "SC3:$_\n";
			}
		}
	}
	return %loc_sc3;
}

##############################################################################################
# ComputeSR1: Creates a hash of hashes with saliency information based on the saliency of both
# concepts and the predicate of a relation. (relation saliency operator 1)
# Takes in a hash of relations.
# Returns a hash of relation saliencies. Keys are concept and predicate, value is {0,1} for
# salient and non-salient concept-predicate pair respectively.
#######################################################################################
sub ComputeSR1 {
	my ( $ref_rels, $ref_concepts ) = @_;
	local (
		%loc_sr1, %loc_temp1, %loc_temp2, %loc_temp3, %avgrel,
		$conc,    $item,      $relation,  $rel_salient
	);
	%loc_rels     = %$ref_rels;
	%loc_concepts = %$ref_concepts;

#first part populate the hash of hash %loc_temp1 which carries the frequencies of rels for each concept
	foreach $conc ( keys %loc_concepts ) {
		if ( $loc_concepts{$conc} == 1 ) {
			foreach $pmid ( keys %loc_rels ) {
				@rel_arr = @{ $loc_rels{$pmid} };
				foreach $rel (@rel_arr) {
					@rel_elements = split( /\|/, $rel );
					if (
						$conc eq
						&Globals::ExtractConcepts( $rel_elements[3], $rel_elements[7] )
						|| $conc eq &Globals::ExtractConcepts(
							$rel_elements[10], $rel_elements[14]
						)
					  )
					{
						$relation = $rel_elements[8];
						if ( exists $loc_temp1{$conc}{$relation} ) {
							$loc_temp1{$conc}{$relation}++;
						}
						else {
							$loc_temp1{$conc}{$relation} = 1;
						}
					}
				}
			}
		}
	}

	if ( $debug == 1 ) {
		foreach $conc ( keys %loc_temp1 ) {
			foreach $rel ( keys %{ $loc_temp1{$conc} } ) {
				print FD "SR1_1:$conc:$rel:$loc_temp1{$conc}{$rel}\n";
			}
		}
	}
	$balance_coeff      = ComputeRelationBalanceCoefficient( \%loc_temp1 );
	%balanced_loc_temp1 = BalanceRelations( \%loc_temp1, $balance_coeff );

	## Second  populate the hash of hash %loc_temp2 which carries as values the averages for the other relationships
	foreach $conc ( keys %balanced_loc_temp1 ) {
		%avgrel = &ComputeAvgSumOther( \%{ $balanced_loc_temp1{$conc} } );
		foreach $rel ( keys %avgrel ) {
			$loc_temp2{$conc}{$rel} = $avgrel{$rel};
		}
	}

	if ( $debug == 1 ) {
		foreach $conc ( keys %loc_temp2 ) {
			foreach $rel ( keys %{ $loc_temp2{$conc} } ) {
				print FD "SR1_2:$conc:$rel:$loc_temp2{$conc}{$rel}\n";
			}
		}
	}

	## Final:  populate the hash of hash %loc_temp3 which carries as values the salient values each  relationship
	## for a salient concept (basically comparing (loc_temp1 with loc_temp2) and $rel_salient >= 1
	$rel_salient = 1
	  ; #this number in Udo Hahn is three meaning it needs at least three rels. Can be changed.
	foreach $conc ( keys %balanced_loc_temp1 ) {
		foreach $rel ( keys %{ $balanced_loc_temp1{$conc} } ) {
			if ( ( $balanced_loc_temp1{$conc}{$rel} >= $rel_salient )
				&& (
					$balanced_loc_temp1{$conc}{$rel} > $loc_temp2{$conc}{$rel} )
			  )
			{
				$loc_temp3{$conc}{$rel} = 1;
			}
			else {
				$loc_temp3{$conc}{$rel} = 0;
			}
		}
	}

	if ( $debug == 1 ) {
		foreach $conc ( keys %loc_temp3 ) {
			foreach $rel ( keys %{ $loc_temp3{$conc} } ) {
				if ( $loc_temp3{$conc}{$rel} == 1 ) {
					print FD "SR1:$conc:$rel\n";
				}
			}
		}
	}

	return %loc_temp3;
}

##############################################################################################
# ComputeSRC1: Creates a hash of saliency based on the saliency of predications.
# (predication saliency operator 1)
# Takes in a hash of relations and the average activation weight of predications.
# Returns a hash of predications saliencies. Keys are predications, value is {0,1} for
# salient and non-salient predications respectively.
#######################################################################################
sub ComputeSRC1 {
	my ( $loc_avgactwht, $loc_pred_in ) = @_;
	%loc_preds = %$loc_pred_in;
	local ( $pred, $loc_src1 );
	foreach $pred ( keys %loc_preds ) {
		if ( $loc_preds{$pred} > $loc_avgactwht ) {
			$loc_src1{$pred} = 1;
		}
		else {
			$loc_src1{$pred} = 0;
		}
	}
	print FD "SRC avg. weight: $loc_avgactwht\n";
	if ( $debug == 1 ) {
		foreach ( keys %loc_src1 ) {
			if ( $loc_src1{$_} == 1 ) {
				print FD "SRC1:$_\n";
			}
		}
	}

	return %loc_src1;
}

##############################################################################################
# IncorpSCs: Incorporates the concept saliency hashes with the list of predications and
# returns a hash of predications. Key is sentence id, value is an array of predications associated
# with that sentence id.
#######################################################################################
sub IncorpSC {
	my ( $ref_rels, $ref_sc1, $ref_sc2, $ref_sc3 ) = @_;
	local ( $rel, @rel_elements, %loc_final_rels );

	#incorporating concept saliency
	%loc_rels = %{$ref_rels};
	%loc_sc1  = %{$ref_sc1};
	%loc_sc2  = %{$ref_sc2};
	%loc_sc3  = %{$ref_sc3};

	#compare predications to concept saliency hashes
	foreach $pmid ( keys %loc_rels ) {
		@rel_arr = @{ $loc_rels{$pmid} };
		undef @incorp_SC_arr;
		foreach $rel (@rel_arr) {
			@rel_elements = split( /\|/, $rel );
			$arg1 = &Globals::ExtractConcepts( $rel_elements[3],  $rel_elements[7] );
			$arg2 = &Globals::ExtractConcepts( $rel_elements[10], $rel_elements[14] );
			if (
				( $loc_sc1{$arg1} == 1 || $loc_sc3{$arg1} == 1 )
				&& (   $loc_sc1{$arg2} == 1
					|| $loc_sc3{$arg2} == 1 )
			  )
			{
				push @incorp_SC_arr, $rel;
			}
		}
		if ( $#incorp_SC_arr > -1 ) {
			$loc_final_rels{$pmid} = [@incorp_SC_arr];
		}
	}

	return %loc_final_rels;
}

##############################################################################################
# IncorpSR: Incorporates the relation saliency hash with the list of predications and
# returns a hash of predications. Key is sentence id, value is an array of predications associated
# with that sentence id.
##############################################################################################
sub IncorpSR {
	my ( $ref_rels, $ref_sr1 ) = @_;
	local ( $rel, $pmid, @rel_elements, @rel_arr, @incorp_SR_arr, %loc_rels,
		%loc_sr1, %loc_final_rels );

	#incorporating relation saliency saliency
	%loc_rels = %{$ref_rels};
	%loc_sr1  = %{$ref_sr1};

	#compare predications to relations  saliency hashes
	foreach $pmid ( keys %loc_rels ) {
		@rel_arr = @{ $loc_rels{$pmid} };
		undef @incorp_SR_arr;
		foreach $rel (@rel_arr) {
			@rel_elements = split( /\|/, $rel );
			$arg1 = &Globals::ExtractConcepts( $rel_elements[3],  $rel_elements[7] );
			$arg2 = &Globals::ExtractConcepts( $rel_elements[10], $rel_elements[14] );
			if (   $loc_sr1{$arg1}{ $rel_elements[8] } == 1
				|| $loc_sr1{$arg2}{ $rel_elements[8] } == 1 )
			{
				push @incorp_SR_arr, $rel;
			}
		}
		if ( $#incorp_SR_arr > -1 ) {
			$loc_final_rels{$pmid} = [@incorp_SR_arr];
		}
	}

	return %loc_final_rels;
}

##############################################################################################
# IncorpSRC: Incorporates the predication saliency hash with the list of predications and
# returns a hash of predications. Key is sentence id, value is an array of predications associated
# with that sentence id.
##############################################################################################
sub IncorpSRC {
	my ( $ref_preds, $ref_src1 ) = @_;
	local ( $item, @itemdecomp, @loc_final_rels );

	#incorporating predication saliency
	%loc_preds = %{$ref_preds};
	%loc_src1  = %{$ref_src1};

	#compare predications to relations  saliency hashes
	foreach $pmid ( keys %loc_preds ) {
		@rel_arr = @{ $loc_preds{$pmid} };
		undef @incorp_SRC_arr;
		foreach $rel (@rel_arr) {
			@rel_elements = split( /\|/, $rel );
			$rel_temp =
			    &Globals::ExtractConcepts( $rel_elements[3], $rel_elements[7] ) . "|"
			  . $rel_elements[8] . "|"
			  . &Globals::ExtractConcepts( $rel_elements[10], $rel_elements[14] );
			if ( $loc_src1{$rel_temp} == 1 ) {
				push @incorp_SRC_arr, $rel;
			}
		}
		if ( $#incorp_SRC_arr > -1 ) {
			$loc_final_preds{$pmid} = [@incorp_SRC_arr];
		}
	}

	return %loc_final_preds;
}

########################## Helper Subroutines ###################################

#################################################################################
# CountConcepts: Computes the frequencies of concepts from a hash of relations.
# Takes in a hash of relations, return a hash of frequencies.
# Key is concept, value is the frequency.
#################################################################################
sub CountConcepts {
	my ($rel_in) = @_;
	my (%rels)   = %$rel_in;
	undef %concept_counts;
	foreach $pmid ( keys %rels ) {
		@rel_arr = @{ $rels{$pmid} };
		foreach $rel (@rel_arr) {
			@rel_elements = split( /\|/, $rel );
			$subj = &Globals::ExtractConcepts( $rel_elements[3],  $rel_elements[7] );
			$obj  = &Globals::ExtractConcepts( $rel_elements[10], $rel_elements[14] );

			#subject
			if ( exists( $concept_counts{$subj} ) ) {
				$concept_counts{$subj}++;
			}
			else {
				$concept_counts{$subj} = 1;
			}

			#object
			if ( exists( $concept_counts{$obj} ) ) {
				$concept_counts{$obj}++;
			}
			else {
				$concept_counts{$obj} = 1;
			}
		}
	}

	if ( $debug == 1 ) {
		foreach (
			sort { $concept_counts{$b} <=> $concept_counts{$a} }
			keys %concept_counts
		  )
		{
			print FD "CountConcepts:$_:$concept_counts{$_}\n";
                        #print "CountConcepts:$_:$concept_counts{$_}\n";
		}
	}

	return %concept_counts;
}

####################################################################################
# CountConceptArray: Computes the frequencies of concepts from an array of relations.
# Takes in an array of relations, return a hash of frequencies.
# Key is concept, value is the frequency.
####################################################################################
sub CountConceptArray {
	my ($rel_in) = @_;
	my (@rels)   = @$rel_in;
	%concept_counts = ();
	foreach $rel (@rels) {
		@rel_elements = split( /\|/, $rel );

		#subject
		if ( exists( $concept_counts{ $rel_elements[0] } ) ) {
			$concept_counts{ $rel_elements[0] }++;
		}
		else {
			$concept_counts{ $rel_elements[0] } = 1;
		}

		#object
		if ( exists( $concept_counts{ $rel_elements[2] } ) ) {
			$concept_counts{ $rel_elements[2] }++;
		}
		else {
			$concept_counts{ $rel_elements[2] } = 1;
		}
	}

	if ( $debug == 1 ) {
		foreach ( keys %concept_counts ) {
			print FD "CountConceptArray:$_:$concept_counts{$_}\n";
                        #print "CountConceptArray:$_:$concept_counts{$_}\n";
		}
	}

	return %concept_counts;
}

####################################################################################
# ComputeAvgActivationWeight: Computes average activation weight for a hash of
# concept frequencies.
# Returns a numeric value.
####################################################################################
sub ComputeAvgActivationWeight {
	my ($concept_in) = @_;
	%concepts = %$concept_in;
	local ( $loc_avgactwht, $loc_totactwht, $conc );
	@conceptlist   = keys %concepts;
	$loc_totactwh  = 0;
	$loc_totccount = 0;
	foreach $conc ( keys %concepts ) {
		$loc_totactwh += $concepts{$conc};
		$loc_totccount++;

	}

	#deals with illegal division
	if ( $loc_totccount == 0 ) {
		return $loc_totactwh;
	}
	else {
		$loc_avgactwht = $loc_totactwh / $loc_totccount;
		return $loc_avgactwht;
	}
}

####################################################################################
# ComputeAvgSumOther: For a given concept, computes average activation weight for the
# rest of the concepts in a hash of concepts.
# Returns a hash of activation weights.
####################################################################################
sub ComputeAvgSumOther {
	my ($loc_concepts_in) = @_;
	%loc_concepts = %$loc_concepts_in;
	local ( $loc_avgsumother, %temp_concepts, %loc_concepts_avgsumother );
	foreach $conc ( keys %loc_concepts ) {
		%temp_concepts = %loc_concepts;
		delete( $temp_concepts{$conc} );
		$loc_avgsumother = &ComputeAvgActivationWeight( \%temp_concepts );
		$loc_concepts_avgsumother{$conc} = $loc_avgsumother;
	}

	if ( $debug == 1 ) {
		foreach ( keys %loc_concepts_avgsumother ) {
			print FD "ComputeAvgSumOther:$_:$loc_concepts_avgsumother{$_}\n";
                	#print "ComputeAvgSumOther:$_:$loc_concepts_avgsumother{$_}\n";
		}
	}

	return %loc_concepts_avgsumother;
}

######################################################################################
# CountPredications: Computes the frequency of predications in a hash of predications.
# Takes in a hash of predications and returns a hash of predication counts.
######################################################################################
sub CountPredications {
	my ($loc_pred_in) = @_;
	%loc_preds = %$loc_pred_in;
	local ( %loc_predications, $item );

	foreach $pmid ( keys %loc_preds ) {
		@rel_arr = @{ $loc_preds{$pmid} };
		foreach $rel (@rel_arr) {
			@rel_elements = split( /\|/, $rel );
			$rel_temp =
			    &Globals::ExtractConcepts( $rel_elements[3], $rel_elements[7] ) . "|"
			  . $rel_elements[8] . "|"
			  . &Globals::ExtractConcepts( $rel_elements[10], $rel_elements[14] );
			if ( exists $loc_predications{$rel_temp} ) {
				$loc_predications{$rel_temp}++;
			}
			else {
				$loc_predications{$rel_temp} = 1;
			}
		}
	}

	if ( $debug == 1 ) {
		foreach ( keys %loc_predications ) {
			print FD "CountPredications:$_:$loc_predications{$_}\n";
		}
	}

	return %loc_predications;
}

######################################################################################
# ComputeRelationBalanceCoefficient: Computes the coefficient that will be used to
# balance dominant predicates with the others.
######################################################################################
sub ComputeRelationBalanceCoefficient {
	my ($loc_rel_in) = @_;
	%loc_rels = %$loc_rel_in;
	local ( %predicate_count_hash, $balance_coeff );
	foreach $conc ( keys %loc_rels ) {
		%rel_hash = %{ $loc_rels{$conc} };
		foreach $rel ( keys %rel_hash ) {
			$cnt = $rel_hash{$rel};
			if ( $rel eq "PREVENTS" ) {
				$rel = "TREATS";
			}
			if ( exists $predicate_count_hash{$rel} ) {
				$predicate_count_hash{$rel} += $cnt;
			}
			else {
				$predicate_count_hash{$rel} = $cnt;
			}
		}
	}
	$dominant_count          = 0;
	$others_count              = 0;
	$dominant_distinct_count = 0;
	$others_distinct_count     = 0;

	foreach $pred ( keys %predicate_count_hash ) {
		if ( exists $dominant_preds{$pred}) {
			$dominant_count += $predicate_count_hash{$pred};
			$dominant_distinct_count++;
		}
		else {
			$others_count += $predicate_count_hash{$pred};
			$others_distinct_count++;
		}
	}

	if ($dominant_distinct_count > 0 && $others_count > 0) {
		$balance_coeff = sprintf( "%d",
			( $dominant_count / $dominant_distinct_count ) /
		  	( $others_count / $others_distinct_count ) ) + 1;
	}
	else {
		$balance_coeff = 1;
	}

	if ( $debug == 1 ) {
		foreach ( keys %predicate_count_hash ) {
			print FD
"ComputerelationBalanceCoefficient:$_:$predicate_count_hash{$_}\n";
		}
		print FD
"ComputeRelationBalanceCoefficient:DOMINANT count: $dominant_count|$dominant_distinct_count\n";
		print FD
"ComputeRelationBalanceCoefficient:Others count: $others_count|$others_distinct_count\n";
		print FD
"ComputeRelationBalanceCoefficient:Balance Coefficient: $balance_coeff\n";
	}

	return $balance_coeff;
}

######################################################################################
# BalanceRelations: Balance the relations with the coefficient computed.
######################################################################################
sub BalanceRelations {
	my ( $loc_rel_in, $coeff_in ) = @_;
	%loc_rels = %$loc_rel_in;
	$coeff    = $coeff_in;
	local (%loc_relations);

	foreach $conc ( keys %loc_rels ) {
		%rel_hash = %{ $loc_rels{$conc} };
		foreach $rel ( keys %rel_hash ) {
			$cnt = $rel_hash{$rel};
			unless ( exists $dominant_preds{$rel}) {
				$loc_relations{$conc}{$rel} = $cnt * $coeff;
			}
			else {
				$loc_relations{$conc}{$rel} = $cnt;
			}
		}
	}

	if ( $debug == 1 ) {
		foreach $conc ( keys %loc_relations ) {
			foreach $rel ( keys %{ $loc_relations{$conc} } ) {
				print FD
				  "BalanceRelations:$conc:$rel:$loc_relations{$conc}{$rel}\n";
			}
		}
	}

	return %loc_relations;
}

######################################################################################
# ComputeBalanceCoefficient: Computes the coefficient that will be used to
# balance TREATS-ISA predications with the others. (A solution to the domination problem)
######################################################################################
sub ComputeBalanceCoefficient {
	my ($loc_pred_in) = @_;
	%loc_preds = %$loc_pred_in;
	local ( %predicate_count_hash, $balance_coeff );
	foreach $rel ( keys %loc_preds ) {
		@rel_elements = split( /\|/, $rel );
		$predicate = $rel_elements[1];
		if ( $predicate eq "PREVENTS" ) {
			$predicate = "TREATS";
		}
		if ( exists $predicate_count_hash{$predicate} ) {
			$predicate_count_hash{$predicate} += $loc_preds{$rel};
		}
		else {
			$predicate_count_hash{$predicate} = $loc_preds{$rel};
		}
	}
	$dominant_count          = 0;
	$others_count              = 0;
	$dominant_distinct_count = 0;
	$others_distinct_count     = 0;
	foreach $pred ( keys %predicate_count_hash ) {
		if ( exists $dominant_preds{$pred} ) {
			$dominant_count += $predicate_count_hash{$pred};
			$dominant_distinct_count++;
		}
		else {
			$others_count += $predicate_count_hash{$pred};
			$others_distinct_count++;
		}
	}

	if ($dominant_distinct_count > 0 && $others_count > 0) {
		$balance_coeff = sprintf( "%d",
			( $dominant_count / $dominant_distinct_count ) /
		  	( $others_count / $others_distinct_count ) ) + 1;
	}
	else {
		$balance_coeff = 1;
	}

	if ( $debug == 1 ) {
		foreach ( keys %predicate_count_hash ) {
			print FD "ComputeBalanceCoefficient:$_:$predicate_count_hash{$_}\n";
		}
		print FD
"ComputeBalanceCoefficient:DOMINANT count: $dominant_count|$dominant_distinct_count\n";
		print FD
"ComputeBalanceCoefficient:Others count: $others_count|$others_distinct_count\n";
		print FD
		  "ComputeBalanceCoefficient:Balance Coefficient: $balance_coeff\n";
	}

	return $balance_coeff;
}

######################################################################################
# BalancePredications: Balance the predications with the coefficient computed.
######################################################################################
sub BalancePredications {
	my ( $loc_pred_in, $coeff_in ) = @_;
	%loc_preds = %$loc_pred_in;
	$coeff     = $coeff_in;
	local (%loc_predications);

	foreach $rel ( keys %loc_preds ) {
		@rel_elements = split( /\|/, $rel );
		$predicate    = $rel_elements[1];
		$cnt          = $loc_preds{$rel};
		unless ( exists $dominant_preds{$predicate})
		{
			$loc_predications{$rel} = $cnt * $coeff;
		}
		else {
			$loc_predications{$rel} = $cnt;
		}
	}

	if ( $debug == 1 ) {
		foreach ( keys %loc_predications ) {
			print FD "BalancePredications:$_:$loc_predications{$_}\n";
		}
	}

	return %loc_predications;
}


###################################################################################
# Novelty: Removes predications from the condensate by finding those that contain
# arguments which are not novel. (i.e., high in the hierarchy of some vocabularies.)
# Takes in a hash of connected relations, and returns a hash of novel relations.
###################################################################################
sub Novelty {
  my ($in_rel) = @_;

  my (%rels)   = %$in_rel;
  my %novel_hash;
  undef %novel_hash;

  foreach my $pmid ( keys %rels ) {
    my @rel_arr = @{ $rels{$pmid} };
    #my @novel_arr;
    undef @novel_arr;
    foreach my $rel (@rel_arr) {
      my @rel_elements = split( /\|/, $rel );
      my $subj         = $rel_elements[3];
      my $subj_cui     = $rel_elements[2];
      my $subj_sem     = $rel_elements[5];
      my $obj          = $rel_elements[10];
      my $obj_cui      = $rel_elements[9];
      my $obj_sem      = $rel_elements[12];
      my $predicate    = $rel_elements[8];

     unless ( &Globals::InNoveltyList(@novel_concepts,$subj) == 1 || &Globals::InNoveltyList(@novel_concepts,$obj) == 1 ) {

	my $novel_subj = &Globals::DBCheck($subj_cui);
	my $novel_obj = &Globals::DBCheck($obj_cui);

	if ($novel_subj!=-1 && $novel_obj!=-1){
	  if ( $predicate =~ /CAUSES$/ ) {
	    if ( exists( $cancause{$subj_sem} ) && exists( $disorders{$obj_sem})){
	      push @novel_arr, $rel;
	    }
	  }elsif ($predicate =~ /TREATS$/ ||  $predicate =~ /PREVENTS$/){
	    if ( exists( $cantreat{$subj_sem} ) && exists( $disorders{$obj_sem})){
	      push @novel_arr, $rel;
	    }
	  }elsif ($predicate =~ /LOCATION_OF$/ ) {
	    if ( exists( $canbeloc{$subj_sem} ) && exists( $disorders{$obj_sem})){
	      push @novel_arr, $rel;
	    }
	  }elsif ($predicate =~ /COEXISTS_WITH$/){
	    if ( exists( $disorders{$subj_sem} ) && exists( $disorders{$obj_sem})){
	      push @novel_arr, $rel;
	    }
	  }elsif ($predicate =~ /PROCESS_OF$/){
	    if ( exists( $disorders{$subj_sem} ) && (exists( $disorders{$obj_sem}) || exists( $human{$obj_sem}))){
	      push @novel_arr, $rel;
	    }
	  }elsif ($predicate =~ /ISA$/){
	    if ((exists( $disorders{$subj_sem}) && exists( $disorders{$obj_sem})) ||
		(exists( $orgcause{$subj_sem}) && exists( $orgcause{$obj_sem})) ||
		(exists( $canchem{$subj_sem}) && exists( $canchem{$obj_sem}))){
	      push @novel_arr, $rel;
	    }
	  }
       }
      }
    }
    if ( $#novel_arr > -1 ) {
	$novel_hash{$pmid} = [@novel_arr];
      }
  }
  return %novel_hash;
}
